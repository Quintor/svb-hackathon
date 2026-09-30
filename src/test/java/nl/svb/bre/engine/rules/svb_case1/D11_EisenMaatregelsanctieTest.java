package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.TestObject;
import nl.svb.bre.engine.domain.Waarde;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class D11_EisenMaatregelsanctieTest {

    private final D11_EisenMaatregelsanctie rule = new D11_EisenMaatregelsanctie();

    @Test
    void testGetDefinitionCode() {
        assertThat(rule.getDefinitionCode()).isEqualTo(Definitiecode.D11_EISEN_MAATREGEL_SANCTIE);
    }

    @Test
    void testDependsOn() {
        assertThat(rule.dependsOn())
                .map(Dependency::getDefinitiecode)
                .containsExactlyInAnyOrder(
                        Definitiecode.D111_MEDEWERKINGSVERPLICHTING_OVERTREDEN,
                        Definitiecode.D112_UITKERING_WAARDE_OP_MAATREGEL_SANCTIE,
                        Definitiecode.D113_SVB_ZIET_AF_VAN_HET_OPLEGGEN_VAN_EEN_MAATREGEL
                );
    }

    @MethodSource
    @ParameterizedTest
    void testExecuteRule(Boolean medewerkingsverplichtingAkwOvertreden,
                         Boolean isInstelling,
                         Boolean uitkeringDefinitiefBeeindigd,
                         Boolean svbZietAfVanMaatregel,
                         Boolean expected) {
        var mockTestObject = mock(TestObject.class);
        var ctx = new CalculationContext(null, mockTestObject, null, null);
        ctx.addCalculatedRule(Definitiecode.D111_MEDEWERKINGSVERPLICHTING_OVERTREDEN, new Waarde<>(medewerkingsverplichtingAkwOvertreden, null));
        if (uitkeringDefinitiefBeeindigd != null) {
            ctx.addCalculatedRule(Definitiecode.D112_UITKERING_WAARDE_OP_MAATREGEL_SANCTIE, new Waarde<>(uitkeringDefinitiefBeeindigd, null));
        }
        if (svbZietAfVanMaatregel != null) {
            ctx.addCalculatedRule(Definitiecode.D113_SVB_ZIET_AF_VAN_HET_OPLEGGEN_VAN_EEN_MAATREGEL, new Waarde<>(svbZietAfVanMaatregel, null));
        }

        when(mockTestObject.isInstelling()).thenReturn(isInstelling);

        assertThat(rule.executeRule(ctx).value()).isEqualTo(expected);
    }

    static Stream<Arguments> testExecuteRule() {
        return Stream.of(
                Arguments.of(false, false, null, null, false),
                Arguments.of(true, true, null, null, false),
                Arguments.of(true, false, true, null, false),
                Arguments.of(true, false, false, true, false),
                Arguments.of(true, false, false, false, true)
        );
    }
}
