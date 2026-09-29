package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static nl.svb.bre.domain.enums.Definitiecode.D122_MINIMUMBEDRAG_MAATREGEL;

@Component
public class D122_MinimumbedragMaatregel extends Rule<BigDecimal> {

    @Override
    public Definitiecode getDefinitionCode() {
        return D122_MINIMUMBEDRAG_MAATREGEL;
    }

    @Override
    protected Waarde<BigDecimal> executeRule(CalculationContext calculationContext) {
        return new Waarde<>(new BigDecimal(25), null);
    }
}
