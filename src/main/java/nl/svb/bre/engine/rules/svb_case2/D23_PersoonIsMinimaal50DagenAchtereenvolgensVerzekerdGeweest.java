package nl.svb.bre.engine.rules.svb_case2;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

@Component
public class D23_PersoonIsMinimaal50DagenAchtereenvolgensVerzekerdGeweest extends Rule<Boolean> {
    @Override
    public Definitiecode getDefinitionCode() {
        return Definitiecode.D23_PERSOON_IS_MINIMAAL_50_DAGEN_ACHTEREENVOLGENS_VERZEKERD_GEWEEST;
    }

    @Override
    protected Waarde<Boolean> executeRule(CalculationContext calculationContext) {
        return null;
    }
}
