package nl.svb.bre.engine;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.svb.bre.domain.Grondslag;
import nl.svb.bre.domain.Grondslaggegeven;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.EngineError;
import nl.svb.bre.engine.domain.ExampleObject;
import nl.svb.bre.engine.domain.TestObject;
import nl.svb.bre.engine.errors.CalculationException;
import nl.svb.bre.engine.errors.FunctionalCalculationException;
import nl.svb.bre.engine.rules.Rule;
import nl.svb.bre.repository.DefinitieRepository;
import nl.svb.bre.repository.GrondslagRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
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

    public Grondslag calculateResult(final Definitiecode definitiecode, final TestObject testObject, final LocalDate peildatum) {
        return calculateResults(Set.of(definitiecode), testObject, peildatum);
    }

    public Grondslag calculateResult(final Definitiecode definitiecode, final ExampleObject exampleObject, final LocalDate peildatum) {
        return calculateResults(Set.of(definitiecode), exampleObject, peildatum);
    }

    public Grondslag calculateResults(final Set<Definitiecode> definitiecodes, final TestObject testObject, final LocalDate peildatum) {
        return calculateResults(testObject.persoonId(), definitiecodes, null, testObject, peildatum);
    }

    public Grondslag calculateResults(final Set<Definitiecode> definitiecodes, final ExampleObject exampleObject, final LocalDate peildatum) {
        return calculateResults(exampleObject.persoonId(), definitiecodes, exampleObject, null, peildatum);
    }

    private Grondslag calculateResults(final Long persoonId, final Set<Definitiecode> definitiecodes, final ExampleObject exampleObject, final TestObject testObject, final LocalDate peildatum) {
        final CalculationContext calculationContext = new CalculationContext(exampleObject, testObject, peildatum);
        final List<EngineError> errors = new LinkedList<>();

        return Optional.ofNullable(grondslagRepository.findByPersoonId(persoonId)).orElseGet(() -> {
            Grondslag grondslag = new Grondslag();
            grondslag.setPersoonId(persoonId);

            definitiecodes.stream()
                    .filter(definitiecode -> !calculationContext.isCalculated(definitiecode))
                    .forEach(definitiecode -> {
                        try {
                            execute(grondslag, calculationContext, definitiecode);
                        } catch (final FunctionalCalculationException ex) {
                            handleFunctionalError(errors, ex);
                        }
                    });
            return grondslagRepository.save(grondslag);
        });
    }

    private Grondslaggegeven<?> execute(final Grondslag grondslag, final CalculationContext calculationContext, final Definitiecode definitiecode) {
        Set<Grondslaggegeven<?>> onderliggend = calculationRules.get(definitiecode).dependsOn().stream()
                .filter(dependency -> (dependency.getRequirment() == null || dependency.getRequirment().test(calculationContext)) && !calculationContext.isCalculated(dependency.getDefinitiecode()))
                .map(dependency -> execute(grondslag, calculationContext, dependency.getDefinitiecode()))
                .collect(Collectors.toSet());

        var waarde = calculationRules.get(definitiecode).execute(calculationContext);
        var definitie = definitieRepository.findByDefinitiecode(definitiecode);
        var grondslaggegeven = new Grondslaggegeven<>(null, definitie, onderliggend, waarde.geldigheidsPeriode(), String.valueOf(waarde.value()));
        grondslag.getGrondslaggegevens().add(grondslaggegeven);
        return grondslaggegeven;
    }

    private void handleFunctionalError(final List<EngineError> errors, final CalculationException exception) {
        if (exception instanceof FunctionalCalculationException) {
            errors.add(new EngineError(true, exception, Set.of(((FunctionalCalculationException) exception).getCalculationError())));
        } else {
            throw exception;
        }
    }

}
