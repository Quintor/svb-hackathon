package nl.svb.bre.engine.rules;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.svb_case1.D12121_BedragKinderbijslagPerRelevantKind;
import org.springframework.stereotype.Component;

@Component
public class berekenWoonlandKind2 extends Rule<D12121_BedragKinderbijslagPerRelevantKind.Woonland> {

    @Override
    public Definitiecode getDefinitionCode() {
        return Definitiecode.SVB_WOONLAND_KIND2;
    }

    @Override
    protected Waarde<D12121_BedragKinderbijslagPerRelevantKind.Woonland> executeRule(CalculationContext calculationContext) {
        if (calculationContext.getTestObject().kindInNL2() == null){
           return new Waarde<>(null, null);
        }
        return new Waarde<>(calculationContext.getTestObject().kindInNL2() ? D12121_BedragKinderbijslagPerRelevantKind.Woonland.IN_NL : D12121_BedragKinderbijslagPerRelevantKind.Woonland.BUITEN_NL, null);
    }
}
