package nl.svb.bre.engine.rules.examples;

import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

@Component
public class Example_Electric_Distance_Bicycle extends Rule<Integer> {

    @Override
    public Definitiecode getDefinitionCode() {
        return Definitiecode.EXAMPLE_ELECTRIC_DISTANCE_BICYCLE;
    }

    @Override
    protected Waarde<Integer> executeRule(CalculationContext calculationContext) {
        return new Waarde<>(70, null);
    }
}
