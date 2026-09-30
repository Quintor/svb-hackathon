package nl.svb.bre.engine.rules.examples;

import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class Example_Journey_Bicycle extends Rule<String> {
    @Override
    public Definitiecode getDefinitionCode() {
        return Definitiecode.EXAMPLE_JOURNEY_BICYCLE;
    }

    @Override
    public DependencySet dependsOn() {
        return DependencySet.of(
                Dependency.of(Definitiecode.EXAMPLE_DURATION)
        );
    }

    @Override
    protected Waarde<String> executeRule(CalculationContext calculationContext) {
        Duration duration = getCalculatedValue(calculationContext, Definitiecode.EXAMPLE_DURATION);
        return new Waarde("The journey by bicycle will take " + duration, null);
    }
}
