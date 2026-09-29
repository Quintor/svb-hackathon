package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import static nl.svb.bre.domain.enums.Definitiecode.D1211_MATE_VAN_VERWIJTBAARHEID;
import static nl.svb.bre.domain.enums.Definitiecode.D1212_AANMERKING_UITKERINGSBEDRAG;
import static nl.svb.bre.domain.enums.Definitiecode.D1213_TYPE_MAATREGELSANCTIE;
import static nl.svb.bre.domain.enums.Definitiecode.D121_BASISBEDRAG_MAATREGEL;

@Component
public class D121_BasisbedragMaatregel extends Rule<Object> {
    @Override
    public Definitiecode getDefinitionCode() {
        return D121_BASISBEDRAG_MAATREGEL;
    }

    @Override
    public DependencySet dependsOn() {
        return DependencySet.of(
                Dependency.of(D1211_MATE_VAN_VERWIJTBAARHEID),
                Dependency.of(D1212_AANMERKING_UITKERINGSBEDRAG),
                Dependency.of(D1213_TYPE_MAATREGELSANCTIE)
        );
    }

    @Override
    protected Waarde<Object> executeRule(CalculationContext calculationContext) {
        return null;
    }
}
