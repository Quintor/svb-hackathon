package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.D1211_Verwijtbaarheid;
import nl.svb.bre.domain.enums.D12131_RecidiveSoort;
import nl.svb.bre.domain.enums.D1213_Maatregel;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.Waarde;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static nl.svb.bre.domain.enums.Definitiecode.D11_EISEN_MAATREGEL_SANCTIE;
import static nl.svb.bre.domain.enums.Definitiecode.D1211_MATE_VAN_VERWIJTBAARHEID;
import static nl.svb.bre.domain.enums.Definitiecode.D12131_RECIDIVE;
import static nl.svb.bre.domain.enums.Definitiecode.D1213_TYPE_MAATREGELSANCTIE;
import static nl.svb.bre.domain.enums.Definitiecode.D12_BASISBEDRAG_IS_LAGER_DAN_MINIMUM_BEDRAG;
import static org.assertj.core.api.Assertions.assertThat;

class D1_DuurEnHoogteMaatregelsanctieTest {

    private final D1_DuurEnHoogteMaatregelsanctie rule = new D1_DuurEnHoogteMaatregelsanctie();

    @Test
    void testGetDefinitionCode() {
        assertThat(rule.getDefinitionCode()).isEqualTo(Definitiecode.D1_DUUR_HOOGTE_MAATREGEL);
    }

    @Test
    void testDependsOn() {
        assertThat(rule.dependsOn())
                .map(Dependency::getDefinitiecode)
                .containsExactlyInAnyOrder(
                        D11_EISEN_MAATREGEL_SANCTIE,
                        D1211_MATE_VAN_VERWIJTBAARHEID,
                        D12_BASISBEDRAG_IS_LAGER_DAN_MINIMUM_BEDRAG,
                        D1213_TYPE_MAATREGELSANCTIE,
                        D12131_RECIDIVE
                );
    }

    @MethodSource
    @ParameterizedTest
    void testExecuteRule(Boolean eisenMaatregelSanctie,
                         D1213_Maatregel maatregel,
                         Boolean basisbedragIsLagerDanMinimum,
                         D12131_RecidiveSoort soortRecidive,
                         D1211_Verwijtbaarheid verwijtbaarheid,
                         String expected) {
        var ctx = new CalculationContext(null, null, null, null);
        ctx.addCalculatedRule(D11_EISEN_MAATREGEL_SANCTIE, new Waarde<>(eisenMaatregelSanctie, null));
        if (maatregel != null) {
            ctx.addCalculatedRule(Definitiecode.D1213_TYPE_MAATREGELSANCTIE, new Waarde<>(maatregel, null));
        }
        if (basisbedragIsLagerDanMinimum != null) {
            ctx.addCalculatedRule(Definitiecode.D12_BASISBEDRAG_IS_LAGER_DAN_MINIMUM_BEDRAG, new Waarde<>(basisbedragIsLagerDanMinimum, null));
        }
        if (soortRecidive != null) {
            ctx.addCalculatedRule(Definitiecode.D12131_RECIDIVE, new Waarde<>(soortRecidive, null));
        }
        if (verwijtbaarheid != null) {
            ctx.addCalculatedRule(D1211_MATE_VAN_VERWIJTBAARHEID, new Waarde<>(verwijtbaarheid, null));
        }

        assertThat(rule.executeRule(ctx).value()).isEqualTo(expected);
    }

    static Stream<Arguments> testExecuteRule() {
        return Stream.of(
                Arguments.of(false, null, null, null, null, "geen maatregel/maatregelwaarschuwing"),
                Arguments.of(true, D1213_Maatregel.WAARSCHUWING, null, null, null, "maatregelwaarschuwing"),
                Arguments.of(true, D1213_Maatregel.SANCTIE_EERSTE_CATEGORIE, true, D12131_RecidiveSoort.GEEN_RECIDIVE, null, "maatregel eerste categorie ter hoogte van het minimumbedrag van de maatregel gedurende één uitkeringstermijn"),
                Arguments.of(true, D1213_Maatregel.SANCTIE_EERSTE_CATEGORIE, true, D12131_RecidiveSoort.RECIDIVE_NA_MAATREGEL, null, "maatregel eerste categorie ter hoogte van anderhalf keer het minimumbedrag van de maatregel gedurende één uitkeringstermijn"),
                Arguments.of(true, D1213_Maatregel.SANCTIE_EERSTE_CATEGORIE, false, D12131_RecidiveSoort.GEEN_RECIDIVE, D1211_Verwijtbaarheid.VERMINDERD_VERWIJTBAAR, "maatregel eerste categorie ter hoogte van 2% van het In aanmerking te nemen uitkeringsbedrag gedurende één uitkeringstermijn"),
                Arguments.of(true, D1213_Maatregel.SANCTIE_EERSTE_CATEGORIE, false, D12131_RecidiveSoort.GEEN_RECIDIVE, D1211_Verwijtbaarheid.VOLLEDIG_VERWIJTBAAR, "maatregel eerste categorie ter hoogte van 5% van het In aanmerking te nemen uitkeringsbedrag gedurende één uitkeringstermijn"),
                Arguments.of(true, D1213_Maatregel.SANCTIE_EERSTE_CATEGORIE, false, D12131_RecidiveSoort.RECIDIVE_NA_MAATREGEL, D1211_Verwijtbaarheid.VOLLEDIG_VERWIJTBAAR, "maatregel eerste categorie ter hoogte van 7.5% van het In aanmerking te nemen uitkeringsbedrag gedurende één uitkeringstermijn"),
                Arguments.of(true, D1213_Maatregel.SANCTIE_EERSTE_CATEGORIE, false, D12131_RecidiveSoort.RECIDIVE_NA_MAATREGEL, D1211_Verwijtbaarheid.VERMINDERD_VERWIJTBAAR, "maatregel eerste categorie ter hoogte van 3% van het In aanmerking te nemen uitkeringsbedrag gedurende één uitkeringstermijn"),
                Arguments.of(true, D1213_Maatregel.SANCTIE_TWEEDE_CATEGORIE, true, D12131_RecidiveSoort.GEEN_RECIDIVE, null, "maatregel tweede categorie ter hoogte van het minimumbedrag van de maatregel gedurende twee uitkeringstermijnen"),
                Arguments.of(true, D1213_Maatregel.SANCTIE_TWEEDE_CATEGORIE, true, D12131_RecidiveSoort.RECIDIVE_NA_MAATREGEL, null, "maatregel tweede categorie ter hoogte van anderhalf keer het minimumbedrag van de maatregel gedurende twee uitkeringstermijnen"),
                Arguments.of(true, D1213_Maatregel.SANCTIE_TWEEDE_CATEGORIE, false, D12131_RecidiveSoort.GEEN_RECIDIVE, D1211_Verwijtbaarheid.VERMINDERD_VERWIJTBAAR, "maatregel tweede categorie ter hoogte van 5% van het In aanmerking te nemen uitkeringsbedrag gedurende twee uitkeringstermijnen"),
                Arguments.of(true, D1213_Maatregel.SANCTIE_TWEEDE_CATEGORIE, false, D12131_RecidiveSoort.RECIDIVE_NA_MAATREGEL, D1211_Verwijtbaarheid.VERMINDERD_VERWIJTBAAR, "maatregel tweede categorie ter hoogte van 7.5% van het In aanmerking te nemen uitkeringsbedrag gedurende twee uitkeringstermijnen"),
                Arguments.of(true, D1213_Maatregel.SANCTIE_TWEEDE_CATEGORIE, false, D12131_RecidiveSoort.GEEN_RECIDIVE, D1211_Verwijtbaarheid.VOLLEDIG_VERWIJTBAAR, "maatregel tweede categorie ter hoogte van 10% van het In aanmerking te nemen uitkeringsbedrag gedurende twee uitkeringstermijnen"),
                Arguments.of(true, D1213_Maatregel.SANCTIE_TWEEDE_CATEGORIE, false, D12131_RecidiveSoort.RECIDIVE_NA_MAATREGEL, D1211_Verwijtbaarheid.VOLLEDIG_VERWIJTBAAR, "maatregel tweede categorie ter hoogte van 15% van het In aanmerking te nemen uitkeringsbedrag gedurende twee uitkeringstermijnen")
        );
    }
}
