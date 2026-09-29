package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import java.util.Set;

import static java.util.function.Predicate.not;
import static nl.svb.bre.domain.enums.Definitiecode.D1111_OVERTREDEN_SOORT_VERPLICHTING_AKW;
import static nl.svb.bre.domain.enums.Definitiecode.D1112_UITZONDERING_MEDEWERKINGSVERPLICHTING_AKW;
import static nl.svb.bre.domain.enums.Definitiecode.D111_MEDEWERKINGSVERPLICHTING_OVERTREDEN;
import static nl.svb.bre.engine.utils.CalculationEnginePredicate.isMedewerkingsplichtigVoorAKW;
import static nl.svb.bre.engine.utils.CalculationEnginePredicate.isuitzonderingMedewerkingsverplichtingAKW;

@Component
public class D111_MedewerkingsverplichtingAKWOvertreden extends Rule<Boolean> {

    private static final Set<String> UITGEZONDERDE_VERPLICHTINGEN = Set.of(
            "reageren op een informatieverzoek",
            "nakomen overige controlevoorschriften",
            "nakomen verplichting tweede categorie");

    @Override
    public Definitiecode getDefinitionCode() {
        return D111_MEDEWERKINGSVERPLICHTING_OVERTREDEN;
    }

    @Override
    public DependencySet dependsOn() {
        return DependencySet.of(
                Dependency.of(D1112_UITZONDERING_MEDEWERKINGSVERPLICHTING_AKW, isMedewerkingsplichtigVoorAKW),
                Dependency.of(D1111_OVERTREDEN_SOORT_VERPLICHTING_AKW, isMedewerkingsplichtigVoorAKW.and(not(isuitzonderingMedewerkingsverplichtingAKW)))
        );
    }

    @Override
    protected Waarde<Boolean> executeRule(CalculationContext calculationContext) {
        var isMedewerkingsplichtigAkw = calculationContext.getTestObject().isMedewerkingsplichtigAkw();
        if (!isMedewerkingsplichtigAkw) {
            return new Waarde<>(false, null);
        }

        if (calculationContext.isCalculated(D1111_OVERTREDEN_SOORT_VERPLICHTING_AKW)) {
            String overtredenSoortVerplichtingAkw = getCalculatedValue(calculationContext, D1111_OVERTREDEN_SOORT_VERPLICHTING_AKW);
            return new Waarde<>(overtredenSoortVerplichtingAkw == null ? null : UITGEZONDERDE_VERPLICHTINGEN.contains(overtredenSoortVerplichtingAkw), null);
        }
        return new Waarde<>(false, null);
    }
}
