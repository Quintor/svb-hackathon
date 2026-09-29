package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import static nl.svb.bre.domain.enums.Definitiecode.D11312_BETROKKENE_KAN_ER_IN_REGELIJKHEID_VAN_UITGAAN_DAT_DE_SVB_AL_OP_DE_HOOGTE_IS;

@Component
public class D11312_BetrokkeneKanErInRedelijkheidVanUitgaanDatDeSVBAlOpDeHoogteIs extends Rule<Boolean> {
    @Override
    public Definitiecode getDefinitionCode() {
        return D11312_BETROKKENE_KAN_ER_IN_REGELIJKHEID_VAN_UITGAAN_DAT_DE_SVB_AL_OP_DE_HOOGTE_IS;
    }

    @Override
    protected Waarde<Boolean> executeRule(CalculationContext calculationContext) {
        return new Waarde<>(calculationContext.getTestObject().svbWasTijdigOpDeHoogte(), null);
    }
}
