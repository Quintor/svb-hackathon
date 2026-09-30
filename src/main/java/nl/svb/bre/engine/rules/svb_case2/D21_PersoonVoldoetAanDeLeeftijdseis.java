package nl.svb.bre.engine.rules.svb_case2;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

@Component
public class D21_PersoonVoldoetAanDeLeeftijdseis extends Rule<Boolean> {

    @Override
    public Definitiecode getDefinitionCode() {
        return Definitiecode.D21_PERSOON_VOLDOET_AAN_DE_LEEFTIJDSEIS;
    }

    @Override
    protected Waarde<Boolean> executeRule(CalculationContext calculationContext) {
        return new Waarde<>(calculationContext.getTestObject().leeftijd() >= 18, null);
    }
}
