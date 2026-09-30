package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class D122_MinimumbedragMaatregelTest {

    private final D122_MinimumbedragMaatregel rule = new D122_MinimumbedragMaatregel();

    @Test
    void testGetDefinitionCode() {
        assertThat(rule.getDefinitionCode()).isEqualTo(Definitiecode.D122_MINIMUMBEDRAG_MAATREGEL);
    }

    @Test
    void testExecuteRule() {
        var ctx = new CalculationContext(null, null, null, null);

        assertThat(rule.executeRule(ctx).value()).isEqualTo(new BigDecimal(25));
    }
}
