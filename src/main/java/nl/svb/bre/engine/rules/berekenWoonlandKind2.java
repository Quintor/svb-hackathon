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
        if (calculationContext.getTestObject().kindInNl2() == null){
            return Waarde.NIET_TE_BEPALEN();
        }
        return new Waarde<>(calculationContext.getTestObject().kindInNl2() ? D12121_BedragKinderbijslagPerRelevantKind.Woonland.IN_NL : D12121_BedragKinderbijslagPerRelevantKind.Woonland.BUITEN_NL, null);
    }
}
