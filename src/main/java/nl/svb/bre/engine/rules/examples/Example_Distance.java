package nl.svb.bre.engine.rules.examples;

import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

@Component
public class Example_Distance extends Rule<Integer> {

    @Override
    public Definitiecode getDefinitionCode() {
        return Definitiecode.EXAMPLE_DISTANCE;
    }

    @Override
    protected Waarde<Integer> executeRule(CalculationContext calculationContext) {
        return new Waarde<>(calculationContext.getTestObject().distance(), null);
    }
}
