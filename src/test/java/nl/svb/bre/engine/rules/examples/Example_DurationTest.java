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

class Example_DurationTest {

    private final Example_Duration rule = new Example_Duration();

    @Test
    void getType_returnsExampleDuration() {
        assertThat(rule.getDefinitionCode(), is(Definitiecode.EXAMPLE_DURATION));
    }

    @Test
    void executeRule_notElectric_returnsDrivingTimeOnly() {
        CalculationContext context = new CalculationContext(new TestObject(100, false, ExampleVehicle.CAR), LocalDate.of(2024, 1, 1));
        context.addCalculatedRule(Definitiecode.EXAMPLE_ELECTRIC, new Waarde<>(false, null));
        context.addCalculatedRule(Definitiecode.EXAMPLE_DISTANCE, new Waarde<>(100, null));
        context.addCalculatedRule(Definitiecode.EXAMPLE_AVERAGE_SPEED, new Waarde<>(20, null));

        assertThat(rule.execute(context).value(), is(Duration.ofMinutes(300L)));
    }

    @Test
    void executeRule_electric_addsChargingTimeToDrivingTime() {
        CalculationContext context = new CalculationContext(new TestObject(140, true, ExampleVehicle.BICYCLE), LocalDate.of(2024, 1, 1));
        context.addCalculatedRule(Definitiecode.EXAMPLE_ELECTRIC, new Waarde<>(true, null));
        context.addCalculatedRule(Definitiecode.EXAMPLE_DISTANCE, new Waarde<>(140, null));
        context.addCalculatedRule(Definitiecode.EXAMPLE_AVERAGE_SPEED, new Waarde<>(20, null));
        context.addCalculatedRule(Definitiecode.EXAMPLE_CHARGING_TIME, new Waarde<>(Duration.ofMinutes(30L), null));

        assertThat(rule.execute(context).value(), is(Duration.ofMinutes(450L)));
    }
}
