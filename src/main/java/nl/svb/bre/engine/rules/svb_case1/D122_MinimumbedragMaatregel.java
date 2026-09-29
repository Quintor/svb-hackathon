package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import static nl.svb.bre.domain.enums.Definitiecode.D122_MINIMUMBEDRAG_MAATREGEL;

@Component
public class D122_MinimumbedragMaatregel extends Rule<Object> {

    @Override
    public Definitiecode getDefinitionCode() {
        return D122_MINIMUMBEDRAG_MAATREGEL;
    }

    @Override
    protected Waarde<Object> executeRule(CalculationContext calculationContext) {
        return null;
    }
}
