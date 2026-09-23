package nl.svb.bre.engine.rules.examples;

import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.TestObject;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.domain.enums.ExampleVehicle;
import nl.svb.bre.domain.enums.Definitiecode;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class Example_Journey_BicycleTest {

    private final Example_Journey_Bicycle rule = new Example_Journey_Bicycle();

    @Test
    void getType_returnsExampleJourneyBicycle() {
        assertThat(rule.getDefinitionCode(), is(Definitiecode.EXAMPLE_JOURNEY_BICYCLE));
    }

    @Test
    void executeRule_returnsJourneyDescriptionWithDuration() {
        CalculationContext context = new CalculationContext(new TestObject(100, false, ExampleVehicle.BICYCLE), LocalDate.of(2024, 1, 1));
        context.addCalculatedRule(Definitiecode.EXAMPLE_DURATION, new Waarde<>(Duration.ofMinutes(45L), null));

        assertThat(rule.execute(context).value(), is("The journey by bicycle will take PT45M"));
    }
}
