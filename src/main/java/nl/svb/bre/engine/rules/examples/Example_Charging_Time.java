package nl.svb.bre.engine.rules.examples;

import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.domain.enums.CalculationError;
import nl.svb.bre.engine.domain.enums.ExampleVehicle;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.errors.FunctionalCalculationException;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import java.time.Duration;

import static nl.svb.bre.engine.utils.CalculationEnginePredicate.isBicycle;

@Component
public class Example_Charging_Time extends Rule<Duration> {
    @Override
    public Definitiecode getDefinitionCode() {
        return Definitiecode.EXAMPLE_CHARGING_TIME;
    }

    @Override
    public DependencySet dependsOn() {
        return DependencySet.of(
                Dependency.of(Definitiecode.EXAMPLE_VEHICLE),
                Dependency.of(Definitiecode.EXAMPLE_ELECTRIC),
                Dependency.of(Definitiecode.EXAMPLE_DISTANCE),
                Dependency.of(Definitiecode.EXAMPLE_CHARGING_TIME_BICYCLE, isBicycle),
                Dependency.of(Definitiecode.EXAMPLE_ELECTRIC_DISTANCE_BICYCLE, isBicycle)
        );
    }

    @Override
    protected Waarde<Duration> executeRule(CalculationContext calculationContext) {
        ExampleVehicle vehicle = getCalculatedValue(calculationContext, Definitiecode.EXAMPLE_VEHICLE);
        Boolean isElectric = getCalculatedValue(calculationContext, Definitiecode.EXAMPLE_ELECTRIC);
        Integer distance = getCalculatedValue(calculationContext, Definitiecode.EXAMPLE_DISTANCE);

        if (!isElectric) {
            return new Waarde<>(Duration.ofMinutes(0L), null);
        }
        if (vehicle == ExampleVehicle.BICYCLE) {
            Integer electricDistanceBicycle = getCalculatedValue(calculationContext, Definitiecode.EXAMPLE_ELECTRIC_DISTANCE_BICYCLE);
            Integer chargingTime = getCalculatedValue(calculationContext, Definitiecode.EXAMPLE_CHARGING_TIME_BICYCLE);

            var numberOfCharges = distance / electricDistanceBicycle;
            return new Waarde<>(Duration.ofMinutes((long) chargingTime * numberOfCharges), null);
        }
        throw new FunctionalCalculationException(CalculationError.UNKNOWN_VEHICLE);
    }
}
