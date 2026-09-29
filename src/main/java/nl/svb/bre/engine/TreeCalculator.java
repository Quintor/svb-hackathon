package nl.svb.bre.engine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.svb.bre.domain.Grondslag;
import nl.svb.bre.domain.Grondslaggegeven;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.EngineError;
import nl.svb.bre.engine.domain.EngineResult;
import nl.svb.bre.engine.domain.ExampleObject;
import nl.svb.bre.engine.domain.TestObject;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.domain.enums.CalculationError;
import nl.svb.bre.engine.errors.CalculationException;
import nl.svb.bre.engine.errors.FunctionalCalculationException;
import nl.svb.bre.engine.rules.Rule;
import nl.svb.bre.repository.DefinitieRepository;
import nl.svb.bre.repository.GrondslagRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class TreeCalculator {

    private final GrondslagRepository grondslagRepository;
    private final DefinitieRepository definitieRepository;
    private final Map<Definitiecode, Rule<?>> calculationRules;

    public EngineResult calculateResult(final Definitiecode definitiecode, final TestObject testObject, final LocalDate peildatum) {
        return calculateResults(Set.of(definitiecode), testObject, peildatum);
    }

    public EngineResult calculateResult(final Definitiecode definitiecode, final ExampleObject exampleObject, final LocalDate peildatum) {
        return calculateResults(Set.of(definitiecode), exampleObject, peildatum);
    }

    public EngineResult calculateResults(final Set<Definitiecode> definitiecodes, final TestObject testObject, final LocalDate peildatum) {
        return calculateResults(testObject.persoonId(), definitiecodes, null, testObject, peildatum);
    }

    public EngineResult calculateResults(final Set<Definitiecode> definitiecodes, final ExampleObject exampleObject, final LocalDate peildatum) {
        return calculateResults(exampleObject.persoonId(), definitiecodes, exampleObject, null, peildatum);
    }

    private EngineResult calculateResults(final Long persoonId, final Set<Definitiecode> definitiecodes, final ExampleObject exampleObject, final TestObject testObject, final LocalDate peildatum) {
        final CalculationContext calculationContext = new CalculationContext(exampleObject, testObject, peildatum);
        final List<EngineError> errors = new LinkedList<>();

        Grondslag result = Optional.ofNullable(grondslagRepository.findByPersoonId(persoonId)).orElseGet(() -> {
            Grondslag grondslag = new Grondslag();
            grondslag.setPersoonId(persoonId);

            definitiecodes.stream()
                    .filter(definitiecode -> !calculationContext.isCalculated(definitiecode))
                    .forEach(definitiecode -> {
                        try {
                            execute(grondslag, calculationContext, definitiecode, errors);
                        } catch (final FunctionalCalculationException ex) {
                            handleFunctionalError(errors, ex);
                        }
                    });
            return grondslagRepository.save(grondslag);
        });
        return new EngineResult(result, errors);
    }

    private Grondslaggegeven<?> execute(Grondslag grondslag, CalculationContext context,
                                        Definitiecode definitiecode, List<EngineError> errors) {
        var rule = calculationRules.get(definitiecode);

        Set<Grondslaggegeven<?>> onderliggend = rule.dependsOn().stream()
                .filter(dep -> needsCalculation(dep, context))
                .map(dep -> execute(grondslag, context, dep.getDefinitiecode(), errors))
                .collect(Collectors.toSet());

        Waarde<?> waarde = null;
        CalculationError calculationError = null;
        try {
            waarde = rule.execute(context);
        } catch (FunctionalCalculationException ex) {
            handleFunctionalError(errors, ex);
            calculationError = ex.getCalculationError();
        }

        var grondslaggegeven = new Grondslaggegeven<>(
                null,
                definitieRepository.findByDefinitiecode(definitiecode),
                onderliggend,
                waarde != null ? waarde.geldigheidsPeriode() : null,
                waarde != null ? String.valueOf(waarde.value()) : null,
                calculationError);

        grondslag.getGrondslaggegevens().add(grondslaggegeven);
        return grondslaggegeven;
    }

    private boolean needsCalculation(Dependency dependency, CalculationContext context) {
        var requirement = dependency.getRequirment();
        return (requirement == null || requirement.test(context))
                && !context.isCalculated(dependency.getDefinitiecode());
    }

    private void handleFunctionalError(final List<EngineError> errors, final CalculationException exception) {
        if (exception instanceof FunctionalCalculationException) {
            errors.add(new EngineError(true, exception, Set.of(((FunctionalCalculationException) exception).getCalculationError())));
        } else {
            throw exception;
        }
    }

}
