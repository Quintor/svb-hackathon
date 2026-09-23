package nl.svb.bre.engine.rules.examples;

import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.TestObject;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.domain.enums.ExampleVehicle;
import nl.svb.bre.domain.enums.Definitiecode;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

class Example_Avg_SpeedTest {

    private final Example_Avg_Speed rule = new Example_Avg_Speed();

    @Test
    void getType_returnsExampleAverageSpeed() {
        assertThat(rule.getDefinitionCode(), is(Definitiecode.EXAMPLE_AVERAGE_SPEED));
    }

    @Test
    void executeRule_car_returns130() {
        CalculationContext context = contextWith(ExampleVehicle.CAR, false);

        assertThat(rule.execute(context).value(), is(130));
    }

    @Test
    void executeRule_electricBicycle_returns22() {
        CalculationContext context = contextWith(ExampleVehicle.BICYCLE, true);

        assertThat(rule.execute(context).value(), is(22));
    }

    @Test
    void executeRule_nonElectricBicycle_returns18() {
        CalculationContext context = contextWith(ExampleVehicle.BICYCLE, false);

        assertThat(rule.execute(context).value(), is(18));
    }

    private CalculationContext contextWith(final ExampleVehicle vehicle, final boolean electric) {
        CalculationContext context = new CalculationContext(new TestObject(100, electric, vehicle), LocalDate.of(2024, 1, 1));
        context.addCalculatedRule(Definitiecode.EXAMPLE_VEHICLE, new Waarde<>(vehicle, null));
        context.addCalculatedRule(Definitiecode.EXAMPLE_ELECTRIC, new Waarde<>(electric, null));
        return context;
    }
}
