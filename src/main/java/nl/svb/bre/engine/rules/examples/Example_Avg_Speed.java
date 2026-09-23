package nl.svb.bre.engine.rules.examples;

import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.domain.enums.ExampleVehicle;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

@Component
public class Example_Avg_Speed extends Rule<Integer> {

    @Override
    public Definitiecode getDefinitionCode() {
        return Definitiecode.EXAMPLE_AVERAGE_SPEED;
    }

    @Override
    public DependencySet dependsOn() {
        return DependencySet.of(
                Dependency.of(Definitiecode.EXAMPLE_VEHICLE),
                Dependency.of(Definitiecode.EXAMPLE_ELECTRIC)
        );
    }

    @Override
    protected Waarde<Integer> executeRule(CalculationContext calculationContext) {
        ExampleVehicle vehicle = getCalculatedValue(calculationContext, Definitiecode.EXAMPLE_VEHICLE);
        return switch (vehicle) {
            case CAR -> new Waarde<>(130, null);
            case BICYCLE -> {
                if (getCalculatedValue(calculationContext, Definitiecode.EXAMPLE_ELECTRIC)) {
                    yield new Waarde<>(22, null);
                }
                yield new Waarde<>(18, null);
            }
        };
    }
}
