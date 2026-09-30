package nl.svb.bre.engine.rules.svb_case2;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Waarde;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static nl.svb.bre.domain.enums.Definitiecode.D21_PERSOON_VOLDOET_AAN_DE_LEEFTIJDSEIS;
import static nl.svb.bre.domain.enums.Definitiecode.D22_PERSOON_VOLDOET_AAN_DE_INKOMENSEIS;
import static nl.svb.bre.domain.enums.Definitiecode.D23_PERSOON_IS_MINIMAAL_50_DAGEN_ACHTEREENVOLGENS_VERZEKERD_GEWEEST;
import static org.assertj.core.api.Assertions.assertThat;

class D2_PersoonHeeftRechtOpToeslagTest {

    private final D2_PersoonHeeftRechtOpToeslag rule = new D2_PersoonHeeftRechtOpToeslag();

    @Test
    void testGetDefinitionCode() {
        assertThat(rule.getDefinitionCode()).isEqualTo(Definitiecode.D2_PERSOON_HEEFT_RECHT_OP_TOESLAG);
    }

    @ParameterizedTest
    @MethodSource("provideForTestExecuteRule")
    void testExecuteRule(Boolean leeftijdseis, Boolean inkomenseis, Boolean verzekerdeis, boolean expected) {
        var ctx = new CalculationContext(null, null, LocalDate.of(2025, 6, 1), null);
        ctx.addCalculatedRule(D21_PERSOON_VOLDOET_AAN_DE_LEEFTIJDSEIS, new Waarde<>(leeftijdseis, null));
        ctx.addCalculatedRule(D22_PERSOON_VOLDOET_AAN_DE_INKOMENSEIS, new Waarde<>(inkomenseis, null));
        ctx.addCalculatedRule(D23_PERSOON_IS_MINIMAAL_50_DAGEN_ACHTEREENVOLGENS_VERZEKERD_GEWEEST, new Waarde<>(verzekerdeis, null));

        assertThat(rule.executeRule(ctx).value()).isEqualTo(expected);
    }

    static Stream<Arguments> provideForTestExecuteRule() {
        return Stream.of(
                Arguments.of(true, true, true, true),
                Arguments.of(false, true, true, false),
                Arguments.of(true, false, true, false),
                Arguments.of(true, true, false, false),
                Arguments.of(null, true, true, false),
                Arguments.of(true, null, true, false),
                Arguments.of(true, true, null, false),
                Arguments.of(null, null, null, false)
        );
    }
}
