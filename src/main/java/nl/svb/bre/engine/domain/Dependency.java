package nl.svb.bre.engine.domain;

import lombok.Data;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.domain.enums.Definitiecode;

import java.util.function.Predicate;

@Data
public class Dependency {

    private Definitiecode definitiecode;
    private Predicate<CalculationContext> requirment;

    private Dependency(Definitiecode definitiecode) {
        this.definitiecode = definitiecode;
    }

    private Dependency(Definitiecode definitiecode, Predicate<CalculationContext> requirement) {
        this.definitiecode = definitiecode;
        this.requirment = requirement;
    }

    public static Dependency of(Definitiecode definitiecode) {
        return new Dependency(definitiecode);
    }

    public static <T> Dependency of(Definitiecode definitiecode, Predicate<CalculationContext> action) {
        return new Dependency(definitiecode, action);
    }
}

