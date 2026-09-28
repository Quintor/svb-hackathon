package nl.svb.bre.engine.rules.examples;

import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.ExampleObject;
import nl.svb.bre.engine.domain.TestObject;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.domain.enums.CalculationError;
import nl.svb.bre.engine.domain.enums.ExampleVehicle;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.errors.FunctionalCalculationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Example_JourneyTest {

    private final Example_Journey rule = new Example_Journey();

    @Test
    void getType_returnsExampleJourney() {
        assertThat(rule.getDefinitionCode(), is(Definitiecode.EXAMPLE_JOURNEY));
    }

    @Test
    void executeRule_bicycle_returnsJourneyBicycleResult() {
        CalculationContext context = new CalculationContext(new ExampleObject(100, false, ExampleVehicle.BICYCLE), null, LocalDate.of(2024, 1, 1));
        context.addCalculatedRule(Definitiecode.EXAMPLE_VEHICLE, new Waarde<>(ExampleVehicle.BICYCLE, null));
        context.addCalculatedRule(Definitiecode.EXAMPLE_JOURNEY_BICYCLE, new Waarde<>("The journey by bicycle will take PT45M", null));

        assertThat(rule.execute(context).value(), is("The journey by bicycle will take PT45M"));
    }

    @Test
    void executeRule_car_throwsUnknownVehicle() {
        CalculationContext context = new CalculationContext(new ExampleObject(100, false, ExampleVehicle.CAR), null, LocalDate.of(2024, 1, 1));
        context.addCalculatedRule(Definitiecode.EXAMPLE_VEHICLE, new Waarde<>(ExampleVehicle.CAR, null));

        FunctionalCalculationException exception = assertThrows(FunctionalCalculationException.class, () -> rule.execute(context));

        assertThat(exception.getCalculationError(), is(CalculationError.UNKNOWN_VEHICLE));
    }
}
