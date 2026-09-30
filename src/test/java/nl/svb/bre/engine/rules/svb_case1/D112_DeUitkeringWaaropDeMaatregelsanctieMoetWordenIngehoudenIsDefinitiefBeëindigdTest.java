package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.TestObject;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class D112_DeUitkeringWaaropDeMaatregelsanctieMoetWordenIngehoudenIsDefinitiefBeëindigdTest {

    private final D112_DeUitkeringWaaropDeMaatregelsanctieMoetWordenIngehoudenIsDefinitiefBeëindigd rule = new D112_DeUitkeringWaaropDeMaatregelsanctieMoetWordenIngehoudenIsDefinitiefBeëindigd();

    @Test
    void testGetDefinitionCode() {
        assertThat(rule.getDefinitionCode()).isEqualTo(Definitiecode.D112_UITKERING_WAARDE_OP_MAATREGEL_SANCTIE);
    }

    @ParameterizedTest
    @MethodSource("provideForTestExecuteRule")
    void testExecuteRule(Boolean isInhoudingsuitkeringBeeindigd,
                         Boolean uitkeringKanHerleven,
                         Boolean expected) {
        var mockTestObject = mock(TestObject.class);
        var ctx = new CalculationContext(null, mockTestObject, null, null);

        when(ctx.getTestObject().isInhoudingsuitkeringBeeindigd()).thenReturn(isInhoudingsuitkeringBeeindigd);
        when(ctx.getTestObject().uitkeringKanHerleven()).thenReturn(uitkeringKanHerleven);

        assertThat(rule.executeRule(ctx).value()).isEqualTo(expected);
    }

    static Stream<Arguments> provideForTestExecuteRule() {
        return Stream.of(
                Arguments.of(true, false, true),
                Arguments.of(true, true, false),
                Arguments.of(false, null, false)
        );
    }
}
