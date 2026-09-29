package nl.svb.bre.engine.utils;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.enums.ExampleVehicle;

import java.util.function.Predicate;

public final class CalculationEnginePredicate {

    private CalculationEnginePredicate() {
    }

    // SVB
    public static final Predicate<CalculationContext> isMedewerkingsplichtigVoorAKW = c -> c.getTestObject().isMedewerkingsplichtigAkw();
    public static final Predicate<CalculationContext> isuitzonderingMedewerkingsverplichtingAKW = c -> getCalculatedValue(c, Definitiecode.D1112_UITZONDERING_MEDEWERKINGSVERPLICHTING_AKW);
    public static final Predicate<CalculationContext> heeftSvbBoetewaarschuwingVoorZelfdeGedraging = c -> c.getTestObject().heeftSvbBoetewaarschuwingVoorZelfdeGedraging();
    public static final Predicate<CalculationContext> heeftSvbBoeteVoorZelfdeGedraging = c -> c.getTestObject().heeftSvbBoeteVoorZelfdeGedraging();

    // Product
    public static final Predicate<CalculationContext> isBicycle = c -> ExampleVehicle.BICYCLE == getCalculatedValue(c, Definitiecode.EXAMPLE_VEHICLE);
    public static final Predicate<CalculationContext> isCar = c -> ExampleVehicle.CAR == getCalculatedValue(c, Definitiecode.EXAMPLE_VEHICLE);
    public static final Predicate<CalculationContext> isElectric = c -> getCalculatedValue(c, Definitiecode.EXAMPLE_ELECTRIC);

    private static <R> R getCalculatedValue(final CalculationContext c, final Definitiecode definitiecode) {
        if (c.isCalculated(definitiecode)) {
            return (R) c.getCalculated(definitiecode);
        }
        throw new IllegalStateException(definitiecode + " A calculated value was requested that should be triggered earlier. To solve this add the requested RuleType to the dependsOn method of this rule");
    }
}
