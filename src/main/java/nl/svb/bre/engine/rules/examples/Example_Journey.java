package nl.svb.bre.engine.rules.examples;

import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.domain.enums.CalculationError;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.errors.FunctionalCalculationException;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import static nl.svb.bre.engine.utils.CalculationEnginePredicate.isBicycle;

@Component
public class Example_Journey extends Rule<String> {
    @Override
    public Definitiecode getDefinitionCode() {
        return Definitiecode.EXAMPLE_JOURNEY;
    }

    @Override
    public DependencySet dependsOn() {
        return DependencySet.of(
                Dependency.of(Definitiecode.EXAMPLE_VEHICLE),
                Dependency.of(Definitiecode.EXAMPLE_JOURNEY_BICYCLE, isBicycle)
        );
    }

    @Override
    protected Waarde<String> executeRule(CalculationContext calculationContext) {
        if (dependencyOf(calculationContext, Definitiecode.EXAMPLE_JOURNEY_BICYCLE)) {
            return new Waarde<>(getCalculatedValue(calculationContext, Definitiecode.EXAMPLE_JOURNEY_BICYCLE), null);
        }
        throw new FunctionalCalculationException(CalculationError.UNKNOWN_VEHICLE);
    }
}
