package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import static nl.svb.bre.domain.enums.Definitiecode.D121_BASISBEDRAG_MAATREGEL;
import static nl.svb.bre.domain.enums.Definitiecode.D122_MINIMUMBEDRAG_MAATREGEL;
import static nl.svb.bre.domain.enums.Definitiecode.D12_BASISBEDRAG_IS_LAGER_DAN_MINIMUM_BEDRAG;

@Component
public class D12_BasisbedragIsLagerDanMinimumBedrag extends Rule<Boolean> {

    @Override
    public Definitiecode getDefinitionCode() {
        return D12_BASISBEDRAG_IS_LAGER_DAN_MINIMUM_BEDRAG;
    }

    @Override
    public DependencySet dependsOn() {
        return DependencySet.of(
                Dependency.of(D121_BASISBEDRAG_MAATREGEL),
                Dependency.of(D122_MINIMUMBEDRAG_MAATREGEL)
        );
    }

    @Override
    protected Waarde<Boolean> executeRule(CalculationContext calculationContext) {
        // D12 het basisbedrag van de maatregel is lager dan het minimumbedrag van de maatregel
        return null;
    }
}
