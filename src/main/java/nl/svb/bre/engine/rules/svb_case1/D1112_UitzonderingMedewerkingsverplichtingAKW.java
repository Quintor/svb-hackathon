package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

@Component
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
        var uitzonderingMedewerkingsverplichting = this.<Boolean>getCalculatedValue(calculationContext, Definitiecode.D11121_UITZONDERING_MEDEWERKINGSVERPLICHTING_VERHUIZING_BUITENLAND_AKW);
        if (uitzonderingMedewerkingsverplichting == null) {
            return new Waarde<>(null, null);
        } else if (uitzonderingMedewerkingsverplichting) {
            return new Waarde<>(true, null);
        }

        var heeftSvbInhoudingenZvwOfWlzOpgevraagd = calculationContext.getTestObject().heeftSvbInhoudingenZvwOfWlzOpgevraagd();
        if (heeftSvbInhoudingenZvwOfWlzOpgevraagd == null) {
            return new Waarde<>(null, null);
        } else if (heeftSvbInhoudingenZvwOfWlzOpgevraagd) {
            return new Waarde<>(true, null);
        }

        var heeftSvbInformatieGevraagdBijBezwaar = calculationContext.getTestObject().heeftSvbInformatieGevraagdBijBezwaar();
        if (heeftSvbInformatieGevraagdBijBezwaar == null) {
            return new Waarde<>(null, null);
        } else if (heeftSvbInformatieGevraagdBijBezwaar) {
            return new Waarde<>(true, null);
        }

        var isBetalingGestaaktOpVerzoek = calculationContext.getTestObject().isBetalingGestaaktOpVerzoek();
        if (isBetalingGestaaktOpVerzoek == null) {
            return new Waarde<>(null, null);
        }
        return new Waarde<>(isBetalingGestaaktOpVerzoek, null);
    }
}
