package nl.svb.bre.engine.rules.examples;

import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.ExampleObject;
import nl.svb.bre.engine.domain.TestObject;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.domain.enums.ExampleVehicle;
import nl.svb.bre.domain.enums.Definitiecode;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class Example_Electric_Distance_BicycleTest {

    private final Example_Electric_Distance_Bicycle rule = new Example_Electric_Distance_Bicycle();

    @Test
    void getType_returnsExampleElectricDistanceBicycle() {
        assertThat(rule.getDefinitionCode(), is(Definitiecode.EXAMPLE_ELECTRIC_DISTANCE_BICYCLE));
    }

    @Test
    void executeRule_alwaysReturnsSeventy() {
        CalculationContext context = new CalculationContext(new ExampleObject(0, false, ExampleVehicle.BICYCLE), null, LocalDate.of(2024, 1, 1));

        assertThat(rule.execute(context).value(), is(70));
    }
}
