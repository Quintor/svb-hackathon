package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import static nl.svb.bre.domain.enums.Definitiecode.D12131_RECIDIVE;
import static nl.svb.bre.domain.enums.Definitiecode.D1213_TYPE_MAATREGELSANCTIE;

@Component
public class D1213_TypeMaatregelsanctie extends Rule<Object> {
    @Override
    public Definitiecode getDefinitionCode() {
        return D1213_TYPE_MAATREGELSANCTIE;
    }

    @Override
    public DependencySet dependsOn() {
        return DependencySet.of(
                Dependency.of(D12131_RECIDIVE)
        );
    }

    @Override
    protected Waarde<Object> executeRule(CalculationContext calculationContext) {
        return null;
    }
}
