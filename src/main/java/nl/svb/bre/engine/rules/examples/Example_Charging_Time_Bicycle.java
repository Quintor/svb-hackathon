package nl.svb.bre.engine.rules.examples;

import nl.svb.bre.domain.GeldigheidsPeriode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.domain.enums.CalculationError;
import nl.svb.bre.engine.domain.enums.ExampleVehicle;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.errors.FunctionalCalculationException;
import nl.svb.bre.engine.rules.Rule;
import nl.svb.bre.engine.utils.PeriodeUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

@Component
public class Example_Charging_Time_Bicycle extends Rule<Integer> {
    @Override
    public Definitiecode getDefinitionCode() {
        return Definitiecode.EXAMPLE_CHARGING_TIME_BICYCLE;
    }

    private static final GeldigheidsPeriode PERIODE_1 = new GeldigheidsPeriode(LocalDate.MIN, LocalDate.of(2025,1, 1));
    private static final GeldigheidsPeriode PERIODE_2 = new GeldigheidsPeriode(LocalDate.of(2025,1, 1), LocalDate.MAX);

    @Override
    public DependencySet dependsOn() {
        return DependencySet.of(
                Dependency.of(Definitiecode.EXAMPLE_ELECTRIC),
                Dependency.of(Definitiecode.EXAMPLE_VEHICLE)
        );
    }

    @Override
    protected Waarde<Integer> executeRule(CalculationContext calculationContext) {
        ExampleVehicle vehicle = getCalculatedValue(calculationContext, Definitiecode.EXAMPLE_VEHICLE);
        Boolean isElectric = getCalculatedValue(calculationContext, Definitiecode.EXAMPLE_ELECTRIC);

        if (vehicle == ExampleVehicle.BICYCLE && isElectric) {
            if (PeriodeUtil.inPeriod(calculationContext.getPeildatum(), PERIODE_1)) {
                return new Waarde<>(30, PERIODE_1);
            } else {
                return new Waarde<>(40, PERIODE_2);
            }
        }
        throw new FunctionalCalculationException(CalculationError.NO_ELECTRIC_BICYCLE);
    }
}
