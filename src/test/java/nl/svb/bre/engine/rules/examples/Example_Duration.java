package nl.svb.bre.engine.rules.examples;

import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static nl.svb.bre.engine.utils.CalculationEnginePredicate.isElectric;

@Component
public class Example_Duration extends Rule<Duration> {
    @Override
    public Definitiecode getDefinitionCode() {
        return Definitiecode.EXAMPLE_DURATION;
    }

    @Override
    public DependencySet dependsOn() {
        return DependencySet.of(
                Dependency.of(Definitiecode.EXAMPLE_ELECTRIC),
                Dependency.of(Definitiecode.EXAMPLE_DISTANCE),
                Dependency.of(Definitiecode.EXAMPLE_AVERAGE_SPEED),
                Dependency.of(Definitiecode.EXAMPLE_CHARGING_TIME, isElectric)
        );
    }

    @Override
    protected Waarde<Duration> executeRule(CalculationContext calculationContext) {
        Integer distance = getCalculatedValue(calculationContext, Definitiecode.EXAMPLE_DISTANCE);
        Integer avgSpeed = getCalculatedValue(calculationContext, Definitiecode.EXAMPLE_AVERAGE_SPEED);

        Duration drivingTime = Duration.ofMinutes((long) (((double) distance / avgSpeed) * 60));

        Duration chargingTime = dependencyOf(calculationContext, Definitiecode.EXAMPLE_CHARGING_TIME)
            ? getCalculatedValue(calculationContext, Definitiecode.EXAMPLE_CHARGING_TIME)
            : Duration.ofMinutes(0L);

        return new Waarde<>(drivingTime.plus(chargingTime), null);
    }
}
