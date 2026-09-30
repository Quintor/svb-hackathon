package nl.svb.bre.engine.rules.examples;

import nl.svb.bre.domain.Grondslag;
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

class Example_VehicleTest {

    private final Example_Vehicle rule = new Example_Vehicle();

    @Test
    void getType_returnsExampleVehicle() {
        assertThat(rule.getDefinitionCode(), is(Definitiecode.EXAMPLE_VEHICLE));
    }

    @Test
    void executeRule_returnsVehicleFromTestObject() {
        CalculationContext context = new CalculationContext(new ExampleObject(100, false, ExampleVehicle.CAR), null, LocalDate.of(2024, 1, 1), null);

        assertThat(rule.execute(context).value(), is(ExampleVehicle.CAR));
    }
}
