package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Waarde;
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
        return null;
    }
}
