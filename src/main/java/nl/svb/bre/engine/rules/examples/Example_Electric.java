package nl.svb.bre.engine.rules.examples;

import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

@Component
public class Example_Electric extends Rule<Boolean> {

    @Override
    public Definitiecode getDefinitionCode() {
        return Definitiecode.EXAMPLE_ELECTRIC;
    }

    @Override
    protected Waarde<Boolean> executeRule(CalculationContext calculationContext) {
        return new Waarde<>(calculationContext.getTestObject().electric(), null);
    }
}
