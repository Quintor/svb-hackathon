package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import static nl.svb.bre.domain.enums.Definitiecode.D113111_BUITENGEWONE_OMSTANDIGHEID;
import static nl.svb.bre.domain.enums.Definitiecode.D11311_DOOR_EEN_BUITENGEWONE_OMSTANDIGHEID_WAS_HET_NAKOMEN_VAN_DE_VERPLICHTING_FEITELIJK_ONMOGELIJK;

@Component
public class D11311_DoorEenBuitengewoneOmstandigheidWasHetNakomenVanDeVerplichtingFeitelijkOnmogelijk extends Rule<Boolean> {
    @Override
    public Definitiecode getDefinitionCode() {
        return D11311_DOOR_EEN_BUITENGEWONE_OMSTANDIGHEID_WAS_HET_NAKOMEN_VAN_DE_VERPLICHTING_FEITELIJK_ONMOGELIJK;
    }

    @Override
    public DependencySet dependsOn() {
        return DependencySet.of(
                Dependency.of(D113111_BUITENGEWONE_OMSTANDIGHEID)
        );
    }

    @Override
    protected Waarde<Boolean> executeRule(CalculationContext calculationContext) {
        return null;
    }
}
