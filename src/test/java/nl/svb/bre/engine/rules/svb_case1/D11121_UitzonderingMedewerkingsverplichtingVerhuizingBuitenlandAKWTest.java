package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.TestObject;
import nl.svb.bre.engine.domain.enums.CalculationError;
import nl.svb.bre.engine.errors.FunctionalCalculationException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class D11121_UitzonderingMedewerkingsverplichtingVerhuizingBuitenlandAKWTest {

    private final D11121_UitzonderingMedewerkingsverplichtingVerhuizingBuitenlandAKW rule = new D11121_UitzonderingMedewerkingsverplichtingVerhuizingBuitenlandAKW();

    @Test
    void testGetDefinitionCode() {
        assertThat(rule.getDefinitionCode()).isEqualTo(Definitiecode.D11121_UITZONDERING_MEDEWERKINGSVERPLICHTING_VERHUIZING_BUITENLAND_AKW);
    }

    @Test
    void testExecuteRule_uitzonderingMedewerkingsverplichting_nietVanToepassing() {
        var mockTestObject = mock(TestObject.class);
        var ctx = new CalculationContext(null, mockTestObject, null, null);

        when(mockTestObject.isMeldplichtigeVerhuizingBuitenland()).thenReturn(false);

        assertThat(rule.execute(ctx).value()).isFalse();
    }

    @Test
    void testExecuteRule_uitzonderingMedewerkingsverplichting_nietVanToepassingDoorOntijdigMelden() {
        var mockTestObject = mock(TestObject.class);
        var ctx = new CalculationContext(null, mockTestObject, null, null);

        when(mockTestObject.isMeldplichtigeVerhuizingBuitenland()).thenReturn(true);
        when(mockTestObject.verhuizingTijdigGemeldNaVerzoek()).thenReturn(false);

        assertThat(rule.execute(ctx).value()).isFalse();
    }

    @Test
    void testExecuteRule_uitzonderingMedewerkingsverplichting_vanToepassing() {
        var mockTestObject = mock(TestObject.class);
        var ctx = new CalculationContext(null, mockTestObject, null, null);

        when(mockTestObject.isMeldplichtigeVerhuizingBuitenland()).thenReturn(true);
        when(mockTestObject.verhuizingTijdigGemeldNaVerzoek()).thenReturn(true);

        assertThat(rule.execute(ctx).value()).isTrue();
    }

    @Test
    void testExecuteRule_uitzonderingMedewerkingsverplichting_unknownRequiredValue() {
        var mockTestObject = mock(TestObject.class);
        var ctx = new CalculationContext(null, mockTestObject, null, null);

        when(mockTestObject.isMeldplichtigeVerhuizingBuitenland()).thenReturn(true);
        when(mockTestObject.verhuizingTijdigGemeldNaVerzoek()).thenReturn(null);

        var ex = catchThrowableOfType(FunctionalCalculationException.class, () -> rule.execute(ctx));

        assertThat(ex.getCalculationError()).isEqualTo(CalculationError.UNKNOWN_VALUE);
    }
}
