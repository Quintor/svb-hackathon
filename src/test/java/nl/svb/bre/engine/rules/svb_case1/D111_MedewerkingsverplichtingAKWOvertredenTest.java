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

class D111_MedewerkingsverplichtingAKWOvertredenTest {

    private final D111_MedewerkingsverplichtingAKWOvertreden rule = new D111_MedewerkingsverplichtingAKWOvertreden();

    @Test
    void testGetDefinitionCode() {
        assertThat(rule.getDefinitionCode()).isEqualTo(Definitiecode.D111_MEDEWERKINGSVERPLICHTING_OVERTREDEN);
    }

    @Test
    void testDependsOn() {
        assertThat(rule.dependsOn())
                .map(Dependency::getDefinitiecode)
                .containsExactlyInAnyOrder(
                        Definitiecode.D1111_OVERTREDEN_SOORT_VERPLICHTING_AKW,
                        Definitiecode.D1112_UITZONDERING_MEDEWERKINGSVERPLICHTING_AKW
                );
    }

    @ParameterizedTest
    @MethodSource
    void testExecuteRule(Boolean isMedewerkingsplichtigAkw,
                         Boolean uitzonderingMedewerkingsverplichtingAkw,
                         String overtredenSoortVerplichtingAkw,
                         Boolean expected) {
        var mockTestObject = mock(TestObject.class);
        var ctx = new CalculationContext(null, mockTestObject, null, null);
        ctx.addCalculatedRule(Definitiecode.D1112_UITZONDERING_MEDEWERKINGSVERPLICHTING_AKW, new Waarde<>(uitzonderingMedewerkingsverplichtingAkw, null));
        if (Boolean.FALSE.equals(uitzonderingMedewerkingsverplichtingAkw)) {
            ctx.addCalculatedRule(Definitiecode.D1111_OVERTREDEN_SOORT_VERPLICHTING_AKW, new Waarde<>(overtredenSoortVerplichtingAkw, null));
        }

        when(mockTestObject.isMedewerkingsplichtigAkw()).thenReturn(isMedewerkingsplichtigAkw);

        assertThat(rule.executeRule(ctx).value()).isEqualTo(expected);
    }

    static Stream<Arguments> testExecuteRule() {
        return Stream.of(
                Arguments.of(false, null, null, false),
                Arguments.of(true, true, null, false),
                Arguments.of(true, false, "something", false),
                Arguments.of(true, false, "nakomen verplichting tweede categorie", true)
        );
    }
}
