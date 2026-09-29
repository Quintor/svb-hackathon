package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static nl.svb.bre.domain.enums.Definitiecode.D111_MEDEWERKINGSVERPLICHTING_OVERTREDEN;
import static nl.svb.bre.domain.enums.Definitiecode.D112_UITKERING_WAARDE_OP_MAATREGEL_SANCTIE;
import static nl.svb.bre.domain.enums.Definitiecode.D113_SVB_ZIET_AF_VAN_HET_OPLEGGEN_VAN_EEN_MAATREGEL;
import static nl.svb.bre.domain.enums.Definitiecode.D11_EISEN_MAATREGEL_SANCTIE;

@Component
public class D11_EisenMaatregelsanctie extends Rule<Boolean> {

    @Override
    public Definitiecode getDefinitionCode() {
        return D11_EISEN_MAATREGEL_SANCTIE;
    }

    @Override
    public DependencySet dependsOn() {
        return DependencySet.of(
                Dependency.of(D111_MEDEWERKINGSVERPLICHTING_OVERTREDEN),
                Dependency.of(D112_UITKERING_WAARDE_OP_MAATREGEL_SANCTIE),
                Dependency.of(D113_SVB_ZIET_AF_VAN_HET_OPLEGGEN_VAN_EEN_MAATREGEL)
        );
    }

    @Override
    protected Waarde<Boolean> executeRule(CalculationContext calculationContext) {
        return new Waarde<>(true, null);
    }
}
