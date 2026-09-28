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

class Example_ElectricTest {

    private final Example_Electric rule = new Example_Electric();

    @Test
    void getType_returnsExampleElectric() {
        assertThat(rule.getDefinitionCode(), is(Definitiecode.EXAMPLE_ELECTRIC));
    }

    @Test
    void executeRule_returnsElectricFromTestObject() {
        CalculationContext context = new CalculationContext(new ExampleObject(100, true, ExampleVehicle.BICYCLE), null, LocalDate.of(2024, 1, 1));

        assertThat(rule.execute(context).value(), is(true));
    }
}
