package nl.svb.bre.engine.rules;

import lombok.extern.slf4j.Slf4j;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.domain.Waarde;
import org.jspecify.annotations.Nullable;

import java.util.Optional;

@Slf4j
public abstract class Rule<T> {

    public abstract Definitiecode getDefinitionCode();

    public Waarde<T> execute(final CalculationContext calculationContext) {
        var startTime = System.currentTimeMillis();
        var result = executeRule(calculationContext);
        calculationContext.addCalculatedRule(getDefinitionCode(), result);
        var time = System.currentTimeMillis() - startTime;
        log.debug("Calculated rule={}, value={}, time={}", getDefinitionCode(), result, time);
        return result;
    }

    protected abstract Waarde<T> executeRule(final CalculationContext calculationContext);

    public DependencySet dependsOn() {
        return DependencySet.of();
    }

    public boolean dependencyOf(final CalculationContext calculationContext, final Definitiecode definitiecode) {
        final Optional<Boolean> dependency = dependsOn().stream()
                .filter(x -> x.getDefinitiecode() == definitiecode)
                .map(x -> x.getRequirment().test(calculationContext))
                .findFirst();
        return dependency.orElse(false);
    }

    @Nullable
    public <R> R getCalculatedValue(final CalculationContext calculationContext, final Definitiecode definitiecode) {
        if (calculationContext.isCalculated(definitiecode)) {
            return (R) calculationContext.getCalculated(definitiecode);
        }
        throw new IllegalStateException(definitiecode + " A calculated value was requested that should be triggered earlier. To solve this add the requested RuleType to the dependsOn method of this rule");
    }

    public <R> R getCalculatedValue(final CalculationContext calculationContext, final Definitiecode definitiecode, final R defaultValue) {
        if (dependencyOf(calculationContext, definitiecode)) {
            return getCalculatedValue(calculationContext, definitiecode);
        } else {
            return defaultValue;
        }
    }
}
