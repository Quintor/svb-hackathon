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

class D1112_UitzonderingMedewerkingsverplichtingAKWTest {

    private final D1112_UitzonderingMedewerkingsverplichtingAKW rule = new D1112_UitzonderingMedewerkingsverplichtingAKW();

    @Test
    void testGetDefinitionCode() {
        assertThat(rule.getDefinitionCode()).isEqualTo(Definitiecode.D1112_UITZONDERING_MEDEWERKINGSVERPLICHTING_AKW);
    }

    @Test
    void testDependsOn() {
        assertThat(rule.dependsOn())
                .map(Dependency::getDefinitiecode)
                .containsExactlyInAnyOrder(
                        Definitiecode.D11121_UITZONDERING_MEDEWERKINGSVERPLICHTING_VERHUIZING_BUITENLAND_AKW
                );
    }

    @ParameterizedTest
    @MethodSource("provideForTestExecuteRule")
    void testExecuteRule(Boolean uitzonderingMedewerkingsverplichtingVerhuizingBuitenlandAkw,
                         Boolean heeftSvbInhoudingenZvwOfWlzOpgevraagd,
                         Boolean heeftSvbInformatieGevraagdBijBezwaar,
                         Boolean isBetalingGestaaktOpVerzoek,
                         Boolean expected) {
        var mockTestObject = mock(TestObject.class);
        var ctx = new CalculationContext(null, mockTestObject, null);
        ctx.addCalculatedRule(Definitiecode.D11121_UITZONDERING_MEDEWERKINGSVERPLICHTING_VERHUIZING_BUITENLAND_AKW,
                new Waarde<>(uitzonderingMedewerkingsverplichtingVerhuizingBuitenlandAkw, null));

        when(mockTestObject.heeftSvbInhoudingenZvwOfWlzOpgevraagd()).thenReturn(heeftSvbInhoudingenZvwOfWlzOpgevraagd);
        when(mockTestObject.heeftSvbInformatieGevraagdBijBezwaar()).thenReturn(heeftSvbInformatieGevraagdBijBezwaar);
        when(mockTestObject.isBetalingGestaaktOpVerzoek()).thenReturn(isBetalingGestaaktOpVerzoek);

        assertThat(rule.executeRule(ctx).value()).isEqualTo(expected);
    }

    static Stream<Arguments> provideForTestExecuteRule() {
        return Stream.of(
                Arguments.of(true, null, null, null, true),
                Arguments.of(false, true, null, null, true),
                Arguments.of(false, false, true, null, true),
                Arguments.of(false, false, false, true, true),
                Arguments.of(false, false, false, false, false)
        );
    }
}