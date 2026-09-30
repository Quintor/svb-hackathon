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

import java.time.Duration;

import java.time.LocalDate;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;

class Example_Charging_TimeTest {

    private final Example_Charging_Time rule = new Example_Charging_Time();

    @Test
    void getType_returnsExampleChargingTime() {
        assertThat(rule.getDefinitionCode(), is(Definitiecode.EXAMPLE_CHARGING_TIME));
    }

    @Test
    void executeRule_notElectric_returnsZero() {
        CalculationContext context = new CalculationContext(new ExampleObject(210, false, ExampleVehicle.BICYCLE), null, LocalDate.of(2024, 1, 1));
        context.addCalculatedRule(Definitiecode.EXAMPLE_VEHICLE, new Waarde<>(ExampleVehicle.BICYCLE, null));
        context.addCalculatedRule(Definitiecode.EXAMPLE_ELECTRIC, new Waarde<>(false, null));
        context.addCalculatedRule(Definitiecode.EXAMPLE_DISTANCE, new Waarde<>(210, null));

        assertThat(rule.execute(context).value(), is(Duration.ofMinutes(0L)));
    }

    @Test
    void executeRule_electricBicycle_returnsChargingTimeForNumberOfCharges() {
        CalculationContext context = new CalculationContext(new ExampleObject(210, true, ExampleVehicle.BICYCLE), null, LocalDate.of(2024, 1, 1));
        context.addCalculatedRule(Definitiecode.EXAMPLE_VEHICLE, new Waarde<>(ExampleVehicle.BICYCLE, null));
        context.addCalculatedRule(Definitiecode.EXAMPLE_ELECTRIC, new Waarde<>(true, null));
        context.addCalculatedRule(Definitiecode.EXAMPLE_DISTANCE, new Waarde<>(210, null));
        context.addCalculatedRule(Definitiecode.EXAMPLE_ELECTRIC_DISTANCE_BICYCLE, new Waarde<>(70, null));
        context.addCalculatedRule(Definitiecode.EXAMPLE_CHARGING_TIME_BICYCLE, new Waarde<>(30, null));

        assertThat(rule.execute(context).value(), is(Duration.ofMinutes(90L)));
    }

    @Test
    void executeRule_electricCar_throwsUnknownVehicle() {
        CalculationContext context = new CalculationContext(new ExampleObject(210, true, ExampleVehicle.CAR), null, LocalDate.of(2024, 1, 1));
        context.addCalculatedRule(Definitiecode.EXAMPLE_VEHICLE, new Waarde<>(ExampleVehicle.CAR, null));
        context.addCalculatedRule(Definitiecode.EXAMPLE_ELECTRIC, new Waarde<>(true, null));
        context.addCalculatedRule(Definitiecode.EXAMPLE_DISTANCE, new Waarde<>(210, null));

        FunctionalCalculationException exception = assertThrows(FunctionalCalculationException.class, () -> rule.execute(context));

        assertThat(exception.getCalculationError(), is(CalculationError.UNKNOWN_VEHICLE));
    }
}
