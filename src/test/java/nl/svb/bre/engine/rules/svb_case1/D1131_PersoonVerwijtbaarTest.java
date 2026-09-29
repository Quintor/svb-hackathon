package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.Waarde;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;

class D1131_PersoonVerwijtbaarTest {

    private final D1131_PersoonVerwijtbaar rule = new D1131_PersoonVerwijtbaar();

    @Test
    void testGetDefinitionCode() {
        assertThat(rule.getDefinitionCode()).isEqualTo(Definitiecode.D1131_PERSOON_VERWIJTBAAR);
    }

    @Test
    void testDependsOn() {
        assertThat(rule.dependsOn())
                .map(Dependency::getDefinitiecode)
                .containsExactlyInAnyOrder(
                        Definitiecode.D11311_DOOR_EEN_BUITENGEWONE_OMSTANDIGHEID_WAS_HET_NAKOMEN_VAN_DE_VERPLICHTING_FEITELIJK_ONMOGELIJK,
                        Definitiecode.D11312_BETROKKENE_KAN_ER_IN_REGELIJKHEID_VAN_UITGAAN_DAT_DE_SVB_AL_OP_DE_HOOGTE_IS
                );
    }

    @ParameterizedTest
    @ValueSource(booleans = {true, false})
    void testExecuteRule(boolean value) {
        var ctx = new CalculationContext(null, null, null);
        ctx.addCalculatedRule(Definitiecode.D11311_DOOR_EEN_BUITENGEWONE_OMSTANDIGHEID_WAS_HET_NAKOMEN_VAN_DE_VERPLICHTING_FEITELIJK_ONMOGELIJK, new Waarde<>(value, null));
        ctx.addCalculatedRule(Definitiecode.D11312_BETROKKENE_KAN_ER_IN_REGELIJKHEID_VAN_UITGAAN_DAT_DE_SVB_AL_OP_DE_HOOGTE_IS, new Waarde<>(null, null));

        assertThat(rule.executeRule(ctx).value()).isNotEqualTo(value);
    }

    @Test
    void testExecuteRule_rejectsWhenNull() {
        var ctx = new CalculationContext(null, null, null);
        ctx.addCalculatedRule(Definitiecode.D11311_DOOR_EEN_BUITENGEWONE_OMSTANDIGHEID_WAS_HET_NAKOMEN_VAN_DE_VERPLICHTING_FEITELIJK_ONMOGELIJK, new Waarde<>(null, null));
        ctx.addCalculatedRule(Definitiecode.D11312_BETROKKENE_KAN_ER_IN_REGELIJKHEID_VAN_UITGAAN_DAT_DE_SVB_AL_OP_DE_HOOGTE_IS, new Waarde<>(null, null));

        assertThat(rule.executeRule(ctx).value()).isNull();
    }
}