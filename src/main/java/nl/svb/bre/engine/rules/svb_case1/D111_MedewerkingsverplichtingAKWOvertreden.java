package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import static nl.svb.bre.domain.enums.Definitiecode.D1111_OVERTREDEN_SOORT_VERPLICHTING_AKW;
import static nl.svb.bre.domain.enums.Definitiecode.D1112_UITZONDERING_MEDEWERKINGSVERPLICHTING_AKW;
import static nl.svb.bre.domain.enums.Definitiecode.D111_MEDEWERKINGSVERPLICHTING_OVERTREDEN;

@Component
public class D111_MedewerkingsverplichtingAKWOvertreden extends Rule<Boolean> {

    @Override
    public Definitiecode getDefinitionCode() {
        return D111_MEDEWERKINGSVERPLICHTING_OVERTREDEN;
    }

    @Override
    public DependencySet dependsOn() {
        return DependencySet.of(
                Dependency.of(D1111_OVERTREDEN_SOORT_VERPLICHTING_AKW),
                Dependency.of(D1112_UITZONDERING_MEDEWERKINGSVERPLICHTING_AKW)
        );
    }

    @Override
    protected Waarde<Boolean> executeRule(CalculationContext calculationContext) {
        return new Waarde<>(true, null);
    }
}
