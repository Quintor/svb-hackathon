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

class D11311_DoorEenBuitengewoneOmstandigheidWasHetNakomenVanDeVerplichtingFeitelijkOnmogelijkTest {

    private final D11311_DoorEenBuitengewoneOmstandigheidWasHetNakomenVanDeVerplichtingFeitelijkOnmogelijk rule = new D11311_DoorEenBuitengewoneOmstandigheidWasHetNakomenVanDeVerplichtingFeitelijkOnmogelijk();

    @Test
    void testGetDefinitionCode() {
        assertThat(rule.getDefinitionCode()).isEqualTo(Definitiecode.D11311_DOOR_EEN_BUITENGEWONE_OMSTANDIGHEID_WAS_HET_NAKOMEN_VAN_DE_VERPLICHTING_FEITELIJK_ONMOGELIJK);
    }

    @Test
    void testDependsOn() {
        assertThat(rule.dependsOn())
                .map(Dependency::getDefinitiecode)
                .containsExactlyInAnyOrder(Definitiecode.D113111_BUITENGEWONE_OMSTANDIGHEID);
    }

    @ParameterizedTest
    @MethodSource("provideForTestExecuteRule")
    void testExecuteRule(Boolean buitengewoneOmstandigheid,
                         Boolean nakomenFeitelijkOnmogelijk,
                         Boolean expected) {
        var mockTestObject = mock(TestObject.class);
        var ctx = new CalculationContext(null, mockTestObject, null, null);
        ctx.addCalculatedRule(Definitiecode.D113111_BUITENGEWONE_OMSTANDIGHEID, new Waarde<>(buitengewoneOmstandigheid, null));

        when(ctx.getTestObject().nakomenFeitelijkOnmogelijk()).thenReturn(nakomenFeitelijkOnmogelijk);

        assertThat(rule.executeRule(ctx).value()).isEqualTo(expected);
    }

    static Stream<Arguments> provideForTestExecuteRule() {
        return Stream.of(
                Arguments.of(false, null, false),
                Arguments.of(true, false, false),
                Arguments.of(true, true, true)
        );
    }
}
