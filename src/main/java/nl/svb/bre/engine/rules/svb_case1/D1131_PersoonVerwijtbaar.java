package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import static nl.svb.bre.domain.enums.Definitiecode.D11311_DOOR_EEN_BUITENGEWONE_OMSTANDIGHEID_WAS_HET_NAKOMEN_VAN_DE_VERPLICHTING_FEITELIJK_ONMOGELIJK;
import static nl.svb.bre.domain.enums.Definitiecode.D11312_BETROKKENE_KAN_ER_IN_REGELIJKHEID_VAN_UITGAAN_DAT_DE_SVB_AL_OP_DE_HOOGTE_IS;
import static nl.svb.bre.domain.enums.Definitiecode.D1131_PERSOON_VERWIJTBAAR;

@Component
public class D1131_PersoonVerwijtbaar extends Rule<Boolean> {
    @Override
    public Definitiecode getDefinitionCode() {
        return D1131_PERSOON_VERWIJTBAAR;
    }

    @Override
    public DependencySet dependsOn() {
        return DependencySet.of(
                Dependency.of(D11311_DOOR_EEN_BUITENGEWONE_OMSTANDIGHEID_WAS_HET_NAKOMEN_VAN_DE_VERPLICHTING_FEITELIJK_ONMOGELIJK),
                Dependency.of(D11312_BETROKKENE_KAN_ER_IN_REGELIJKHEID_VAN_UITGAAN_DAT_DE_SVB_AL_OP_DE_HOOGTE_IS)
        );
    }

    @Override
    protected Waarde<Boolean> executeRule(CalculationContext calculationContext) {
        return null;
    }
}
