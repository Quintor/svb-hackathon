package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.TestObject;
import nl.svb.bre.engine.domain.enums.CalculationError;
import nl.svb.bre.engine.errors.FunctionalCalculationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowableOfType;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class D1111_OvertredenSoortVerplichtingAKWTest {

    private final D1111_OvertredenSoortVerplichtingAKW rule = new D1111_OvertredenSoortVerplichtingAKW();

    @Test
    void testGetDefinitionCode() {
        assertThat(rule.getDefinitionCode()).isEqualTo(Definitiecode.D1111_OVERTREDEN_SOORT_VERPLICHTING_AKW);
    }

    @ParameterizedTest
    @MethodSource("provideForTestExecuteRule")
    void testExecuteRule(String overtredenAkwVerplichting, String expected) {
        var mockTestObject = mock(TestObject.class);
        var ctx = new CalculationContext(null, mockTestObject, null);

        when(ctx.getTestObject().overtredenAkwVerplichting()).thenReturn(overtredenAkwVerplichting);

        assertThat(rule.executeRule(ctx).value()).isEqualTo(expected);
    }

    @Test
    void testExecuteRule_failsWhenInputUnknown() {
        var ctx = new CalculationContext(null, mock(TestObject.class), null);

        var ex = catchThrowableOfType(FunctionalCalculationException.class, () -> rule.execute(ctx));

        assertThat(ex.getCalculationError()).isEqualTo(CalculationError.MISSING_VALUE_D1111);
    }

    static Stream<Arguments> provideForTestExecuteRule() {
        return Stream.of(
                Arguments.of("Het mogelijk maken van controle door personen die daarmee door de bank zijn belast", "nakomen overige controlevoorschriften"),
                Arguments.of("Het op verzoek verschijnen op een door de SVB aangewezen kantoor om de gevraagde gegevens te verstrekken indien de klant in het buitenland woont", "nakomen verplichting tweede categorie"),
                Arguments.of("Kind < 16: het op verzoek verstrekken van het adres van een uitwonend (wordend) kind", "reageren op een informatieverzoek"),
                Arguments.of("Het op verzoek verstrekken van informatie met behulp van door de SVB ter beschikking gestelde formulieren en het op verzoek overleggen van bewijsstukken binnen de door de SVB gestelde termijn", "reageren op een informatieverzoek")
        );
    }
}
