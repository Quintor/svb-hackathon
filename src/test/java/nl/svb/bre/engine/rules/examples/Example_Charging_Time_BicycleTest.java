package nl.svb.bre.engine.rules.examples;

import nl.svb.bre.engine.context.CalculationContext;
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

class Example_Charging_Time_BicycleTest {

    private final Example_Charging_Time_Bicycle rule = new Example_Charging_Time_Bicycle();

    @Test
    void getType_returnsExampleChargingTimeBicycle() {
        assertThat(rule.getDefinitionCode(), is(Definitiecode.EXAMPLE_CHARGING_TIME_BICYCLE));
    }

    @Test
    void executeRule_electricBicycle_returns30() {
        CalculationContext context = contextWith(ExampleVehicle.BICYCLE, true);

        assertThat(rule.execute(context).value(), is(30));
    }

    @Test
    void executeRule_nonElectricBicycle_throwsNoElectricBicycle() {
        CalculationContext context = contextWith(ExampleVehicle.BICYCLE, false);

        FunctionalCalculationException exception = assertThrows(FunctionalCalculationException.class, () -> rule.execute(context));

        assertThat(exception.getCalculationError(), is(CalculationError.NO_ELECTRIC_BICYCLE));
    }

    @Test
    void executeRule_electricCar_throwsNoElectricBicycle() {
        CalculationContext context = contextWith(ExampleVehicle.CAR, true);

        FunctionalCalculationException exception = assertThrows(FunctionalCalculationException.class, () -> rule.execute(context));

        assertThat(exception.getCalculationError(), is(CalculationError.NO_ELECTRIC_BICYCLE));
    }

    private CalculationContext contextWith(final ExampleVehicle vehicle, final boolean electric) {
        CalculationContext context = new CalculationContext(new TestObject(100, electric, vehicle), LocalDate.of(2024, 1, 1));
        context.addCalculatedRule(Definitiecode.EXAMPLE_VEHICLE, new Waarde<>(vehicle, null));
        context.addCalculatedRule(Definitiecode.EXAMPLE_ELECTRIC, new Waarde<>(electric, null));
        return context;
    }
}
