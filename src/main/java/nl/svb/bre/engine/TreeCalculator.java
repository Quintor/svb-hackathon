package nl.svb.bre.engine;

import lombok.RequiredArgsConstructor;
import nl.svb.bre.domain.GeldigheidsPeriode;
import nl.svb.bre.domain.Grondslag;
import nl.svb.bre.domain.Grondslaggegeven;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.ExampleObject;
import nl.svb.bre.engine.domain.TestObject;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.domain.enums.CalculationError;
import nl.svb.bre.engine.errors.FunctionalCalculationException;
import nl.svb.bre.engine.rules.Rule;
import nl.svb.bre.engine.utils.PeriodeUtil;
import nl.svb.bre.repository.DefinitieRepository;
import nl.svb.bre.repository.GrondslagRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
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
        return calculateResults(testObject.persoonId(), definitiecodes, new CalculationContext(null, testObject, peildatum));
    }

    public Grondslag calculateResults(final Set<Definitiecode> definitiecodes, final ExampleObject exampleObject, final LocalDate peildatum) {
        return calculateResults(exampleObject.persoonId(), definitiecodes, new CalculationContext(exampleObject, null, peildatum));
    }

    private Grondslag calculateResults(final Long persoonId, final Set<Definitiecode> definitiecodes, final CalculationContext context) {
        var grondslag = Optional.ofNullable(grondslagRepository.findByPersoonId(persoonId))
                .orElseGet(() -> new Grondslag(null, persoonId, new HashSet<>()));

        definitiecodes.stream()
                .filter(definitiecode -> !context.isCalculated(definitiecode))
                .forEach(definitiecode -> execute(grondslag, context, definitiecode));

        return grondslagRepository.save(grondslag);
    }

    private Grondslaggegeven<?> execute(final Grondslag grondslag, final CalculationContext context, final Definitiecode definitiecode) {
        var rule = calculationRules.get(definitiecode);

        Set<Grondslaggegeven<?>> onderliggend = rule.dependsOn().stream()
                .filter(dependency -> needsCalculation(dependency, context))
                .map(dependency -> execute(grondslag, context, dependency.getDefinitiecode()))
                .collect(Collectors.toSet());

        Waarde<?> waarde = null;
        CalculationError calculationError = null;
        try {
            waarde = rule.execute(context);
        } catch (final FunctionalCalculationException ex) {
            calculationError = ex.getCalculationError();
            context.addCalculatedRule(definitiecode, Waarde.NIET_TE_BEPALEN());
        }
        var periode = waarde != null ? waarde.geldigheidsPeriode() : null;
        var grondslaggegeven = findOrCreate(grondslag, definitiecode, onderliggend, periode);
        grondslaggegeven.setWaarde(waarde != null ? String.valueOf(waarde.value()) : null);
        grondslaggegeven.setGeldigheidsPeriode(periode);
        grondslaggegeven.setCalculationError(calculationError);

        return grondslaggegeven;
    }

    private Grondslaggegeven<?> findOrCreate(final Grondslag grondslag, final Definitiecode definitiecode, final Set<Grondslaggegeven<?>> onderliggend, final GeldigheidsPeriode periode) {
        return grondslag.getGrondslaggegevens().stream()
                .filter(grondslaggegeven -> grondslaggegeven.getDefinitie().getDefinitiecode() == definitiecode)
                .filter(grondslaggegeven -> Objects.equals(grondslaggegeven.getGeldigheidsPeriode(), periode))
                .findFirst()
                .orElseGet(() -> {
                    var created = new Grondslaggegeven<>(null, definitieRepository.findByDefinitiecode(definitiecode), onderliggend, null, null, null);
                    grondslag.getGrondslaggegevens().add(created);
                    return created;
                });
    }

    private boolean needsCalculation(Dependency dependency, CalculationContext context) {
        var requirement = dependency.getRequirment();
        return (requirement == null || requirement.test(context))
                && !context.isCalculated(dependency.getDefinitiecode());
    }
}
