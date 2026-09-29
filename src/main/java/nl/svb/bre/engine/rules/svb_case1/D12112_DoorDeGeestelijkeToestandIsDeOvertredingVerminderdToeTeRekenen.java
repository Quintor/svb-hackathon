package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.domain.enums.CalculationError;
import nl.svb.bre.engine.errors.FunctionalCalculationException;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import static nl.svb.bre.domain.enums.Definitiecode.D12112_DOOR_DE_GEESTELIJKE_TOESTAND_IS_DE_OVERTREDING_VERMINDERD_TOE_TE_REKENEN;

@Component
public class D12112_DoorDeGeestelijkeToestandIsDeOvertredingVerminderdToeTeRekenen extends Rule<Boolean> {
    @Override
    public Definitiecode getDefinitionCode() {
        return D12112_DOOR_DE_GEESTELIJKE_TOESTAND_IS_DE_OVERTREDING_VERMINDERD_TOE_TE_REKENEN;
    }

    @Override
    protected Waarde<Boolean> executeRule(CalculationContext calculationContext) {
        boolean onbekwaam = calculationContext.getTestObject().administratiefOnbekwaam();
        Boolean b2 = null;

        if(!onbekwaam) {
            return new Waarde<>(onbekwaam, null);
        }
        throw new FunctionalCalculationException(CalculationError.MISSING_VALUE_D12112);
    }
}
