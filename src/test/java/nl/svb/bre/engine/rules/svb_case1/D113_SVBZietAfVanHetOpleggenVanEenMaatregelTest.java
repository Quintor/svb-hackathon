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

class D113_SVBZietAfVanHetOpleggenVanEenMaatregelTest {

    private final D113_SVBZietAfVanHetOpleggenVanEenMaatregel rule = new D113_SVBZietAfVanHetOpleggenVanEenMaatregel();

    @Test
    void testGetDefinitionCode() {
        assertThat(rule.getDefinitionCode()).isEqualTo(Definitiecode.D113_SVB_ZIET_AF_VAN_HET_OPLEGGEN_VAN_EEN_MAATREGEL);
    }

    @Test
    void testDependsOn() {
        assertThat(rule.dependsOn())
                .map(Dependency::getDefinitiecode)
                .containsExactlyInAnyOrder(Definitiecode.D1131_PERSOON_VERWIJTBAAR);
    }

    @MethodSource
    @ParameterizedTest
    void testExecuteRule(Boolean heeftSvbBoetewaarschuwingVoorZelfdeGedraging,
                         Boolean heeftSvbBoeteVoorZelfdeGedraging,
                         Boolean persoonVerwijtbaar,
                         Boolean isDringendeRedenAanwezig,
                         Boolean expected) {
        var mockTestObject = mock(TestObject.class);
        var ctx = new CalculationContext(null, mockTestObject, null, null);
        if (persoonVerwijtbaar != null) {
            ctx.addCalculatedRule(Definitiecode.D1131_PERSOON_VERWIJTBAAR, new Waarde<>(persoonVerwijtbaar, null));
        }

        when(mockTestObject.heeftSvbBoetewaarschuwingVoorZelfdeGedraging()).thenReturn(heeftSvbBoetewaarschuwingVoorZelfdeGedraging);
        when(mockTestObject.heeftSvbBoeteVoorZelfdeGedraging()).thenReturn(heeftSvbBoeteVoorZelfdeGedraging);
        when(mockTestObject.isDringendeRedenAanwezig()).thenReturn(isDringendeRedenAanwezig);

        assertThat(rule.executeRule(ctx).value()).isEqualTo(expected);
    }

    static Stream<Arguments> testExecuteRule() {
        return Stream.of(
                Arguments.of(true, false, null, true, true),
                Arguments.of(false, true, null, true, true),
                Arguments.of(false, false, false, false, true),
                Arguments.of(false, false, true, true, true),
                Arguments.of(false, false, true, false, false)
        );
    }
}
