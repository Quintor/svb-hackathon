package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.TestObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class D11312_BetrokkeneKanErInRedelijkheidVanUitgaanDatDeSVBAlOpDeHoogteIsTest {

    private final D11312_BetrokkeneKanErInRedelijkheidVanUitgaanDatDeSVBAlOpDeHoogteIs rule = new D11312_BetrokkeneKanErInRedelijkheidVanUitgaanDatDeSVBAlOpDeHoogteIs();

    @Test
    void testGetDefinitionCode() {
        assertThat(rule.getDefinitionCode()).isEqualTo(Definitiecode.D11312_BETROKKENE_KAN_ER_IN_REGELIJKHEID_VAN_UITGAAN_DAT_DE_SVB_AL_OP_DE_HOOGTE_IS);
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testExecuteRule(boolean value) {
        var mockTestObject = mock(TestObject.class);
        var ctx = new CalculationContext(null, mockTestObject, null);

        when(ctx.getTestObject().svbWasTijdigOpDeHoogte()).thenReturn(value);

        assertThat(rule.executeRule(ctx).value()).isEqualTo(value);
    }
}