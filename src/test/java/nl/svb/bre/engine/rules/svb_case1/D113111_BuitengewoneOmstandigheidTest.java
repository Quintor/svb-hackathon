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

class D113111_BuitengewoneOmstandigheidTest {

    private final D113111_BuitengewoneOmstandigheid rule = new D113111_BuitengewoneOmstandigheid();

    @Test
    void testGetDefinitionCode() {
        assertThat(rule.getDefinitionCode()).isEqualTo(Definitiecode.D113111_BUITENGEWONE_OMSTANDIGHEID);
    }

    @ParameterizedTest
    @MethodSource("provideForTestExecuteRule")
    void testExecuteRule(Boolean normaalLevenspatroonPersoon,
                         Boolean voorzieneOmstandigheid,
                         Boolean gewensteOmstandigheid,
                         Boolean expected) {
        var mockTestObject = mock(TestObject.class);
        var ctx = new CalculationContext(null, mockTestObject, null, null);

        when(mockTestObject.normaalLevenspatroonPersoon()).thenReturn(normaalLevenspatroonPersoon);
        when(mockTestObject.voorzieneOmstandigheid()).thenReturn(voorzieneOmstandigheid);
        when(mockTestObject.gewensteOmstandigheid()).thenReturn(gewensteOmstandigheid);

        assertThat(rule.executeRule(ctx).value()).isEqualTo(expected);
    }

    static Stream<Arguments> provideForTestExecuteRule() {
        return Stream.of(
                Arguments.of(true, null, null, false),
                Arguments.of(false, true, null, false),
                Arguments.of(false, false, true, false),
                Arguments.of(false, false, false, true)
        );
    }
}
