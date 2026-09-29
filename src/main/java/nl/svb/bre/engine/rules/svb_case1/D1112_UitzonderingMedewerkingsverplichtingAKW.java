package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;

public class D1112_UitzonderingMedewerkingsverplichtingAKW extends Rule<Boolean> {
    @Override
    public Definitiecode getDefinitionCode() {
        return Definitiecode.D1112_UITZONDERING_MEDEWERKINGSVERPLICHTING_AKW;
    }

    @Override
    public DependencySet dependsOn() {
        return DependencySet.of(
                Dependency.of(Definitiecode.D11121_UITZONDERING_MEDEWERKINGSVERPLICHTING_VERHUIZING_BUITENLAND_AKW)
        );
    }
    
    @Override
    protected Waarde<Boolean> executeRule(CalculationContext calculationContext) {
        var result = this.<Boolean>getCalculatedValue(calculationContext, Definitiecode.D11121_UITZONDERING_MEDEWERKINGSVERPLICHTING_VERHUIZING_BUITENLAND_AKW)
                || calculationContext.getTestObject().heeftSvbInhoudingenZvwOfWlzOpgevraagd()
                || calculationContext.getTestObject().heeftSvbInformatieGevraagdBijBezwaar()
                || calculationContext.getTestObject().isBetalingGestaaktOpVerzoek();
        return new Waarde<>(result, null);
    }
}
