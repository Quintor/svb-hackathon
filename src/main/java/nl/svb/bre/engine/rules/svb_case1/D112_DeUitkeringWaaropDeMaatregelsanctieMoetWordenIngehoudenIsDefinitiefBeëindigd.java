package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import static nl.svb.bre.domain.enums.Definitiecode.D112_UITKERING_WAARDE_OP_MAATREGEL_SANCTIE;

@Component
public class D112_DeUitkeringWaaropDeMaatregelsanctieMoetWordenIngehoudenIsDefinitiefBeëindigd extends Rule<Boolean> {
    @Override
    public Definitiecode getDefinitionCode() {
        return D112_UITKERING_WAARDE_OP_MAATREGEL_SANCTIE;
    }

    @Override
    protected Waarde<Boolean> executeRule(CalculationContext calculationContext) {
        return null;
    }
}
