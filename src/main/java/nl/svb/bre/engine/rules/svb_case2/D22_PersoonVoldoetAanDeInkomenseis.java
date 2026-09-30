package nl.svb.bre.engine.rules.svb_case2;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

@Component
public class D22_PersoonVoldoetAanDeInkomenseis extends Rule<Boolean> {

    @Override
    public Definitiecode getDefinitionCode() {
        return Definitiecode.D22_PERSOON_VOLDOET_AAN_DE_INKOMENSEIS;
    }

    @Override
    protected Waarde<Boolean> executeRule(CalculationContext calculationContext) {
        return new Waarde<>(true, null);
    }
}
