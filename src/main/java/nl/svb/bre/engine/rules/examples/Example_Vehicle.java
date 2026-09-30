package nl.svb.bre.engine.rules.examples;

import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.domain.enums.ExampleVehicle;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

@Component
public class Example_Vehicle extends Rule<ExampleVehicle> {
    @Override
    public Definitiecode getDefinitionCode() {
        return Definitiecode.EXAMPLE_VEHICLE;
    }

    @Override
    protected Waarde<ExampleVehicle> executeRule(CalculationContext calculationContext) {
        return new Waarde<>(calculationContext.getExampleObject().vehicle(), null);
    }
}
