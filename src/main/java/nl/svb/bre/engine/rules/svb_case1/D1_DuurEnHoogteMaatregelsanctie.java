package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.D1211_Verwijtbaarheid;
import nl.svb.bre.domain.enums.D12131_RecidiveSoort;
import nl.svb.bre.domain.enums.D1213_Maatregel;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.domain.enums.CalculationError;
import nl.svb.bre.engine.errors.FunctionalCalculationException;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import static nl.svb.bre.domain.enums.Definitiecode.D11_EISEN_MAATREGEL_SANCTIE;
import static nl.svb.bre.domain.enums.Definitiecode.D1211_MATE_VAN_VERWIJTBAARHEID;
import static nl.svb.bre.domain.enums.Definitiecode.D12131_RECIDIVE;
import static nl.svb.bre.domain.enums.Definitiecode.D1213_TYPE_MAATREGELSANCTIE;
import static nl.svb.bre.domain.enums.Definitiecode.D12_BASISBEDRAG_IS_LAGER_DAN_MINIMUM_BEDRAG;
import static nl.svb.bre.domain.enums.Definitiecode.D1_DUUR_HOOGTE_MAATREGEL;

@Component
public class D1_DuurEnHoogteMaatregelsanctie extends Rule<String> {

    @Override
    public Definitiecode getDefinitionCode() {
        return D1_DUUR_HOOGTE_MAATREGEL;
    }

    @Override
    public DependencySet dependsOn() {
        return DependencySet.of(
                Dependency.of(D11_EISEN_MAATREGEL_SANCTIE),
                Dependency.of(D1211_MATE_VAN_VERWIJTBAARHEID),
                Dependency.of(D12_BASISBEDRAG_IS_LAGER_DAN_MINIMUM_BEDRAG),
                Dependency.of(D1213_TYPE_MAATREGELSANCTIE),
                Dependency.of(D12131_RECIDIVE)
        );
    }

    @Override
    protected Waarde<String> executeRule(CalculationContext calculationContext) {
        boolean eisenMaatregelSanctie = getCalculatedValue(calculationContext, D11_EISEN_MAATREGEL_SANCTIE);
        if (!eisenMaatregelSanctie) {
            return new Waarde<>("geen maatregel/maatregelwaarschuwing", null);
        }

        D1213_Maatregel typeMaatregelSanctie = getCalculatedValue(calculationContext, D1213_TYPE_MAATREGELSANCTIE);
        var result = switch (typeMaatregelSanctie) {
            case D1213_Maatregel.WAARSCHUWING -> "maatregelwaarschuwing";
            case D1213_Maatregel.SANCTIE_EERSTE_CATEGORIE -> {
                boolean basisbedragIsLagerDanMinimum = getCalculatedValue(calculationContext, D12_BASISBEDRAG_IS_LAGER_DAN_MINIMUM_BEDRAG);
                D12131_RecidiveSoort recidive = getCalculatedValue(calculationContext, D12131_RECIDIVE);
                if (basisbedragIsLagerDanMinimum) {
                    if (recidive != D12131_RecidiveSoort.RECIDIVE_NA_MAATREGEL) {
                        yield "maatregel eerste categorie ter hoogte van het minimumbedrag van de maatregel gedurende één uitkeringstermijn";
                    } else {
                        yield "maatregel eerste categorie ter hoogte van anderhalf keer het minimumbedrag van de maatregel gedurende één uitkeringstermijn";
                    }
                } else {
                    D1211_Verwijtbaarheid mateVanVerwijtbaarheid = getCalculatedValue(calculationContext, D1211_MATE_VAN_VERWIJTBAARHEID);
                    if (recidive != D12131_RecidiveSoort.RECIDIVE_NA_MAATREGEL) {
                        if (mateVanVerwijtbaarheid == D1211_Verwijtbaarheid.VERMINDERD_VERWIJTBAAR) {
                            yield "maatregel eerste categorie ter hoogte van 2% van het In aanmerking te nemen uitkeringsbedrag gedurende één uitkeringstermijn";
                        } else {
                            yield "maatregel eerste categorie ter hoogte van 5% van het In aanmerking te nemen uitkeringsbedrag gedurende één uitkeringstermijn";
                        }
                    } else {
                        if (mateVanVerwijtbaarheid == D1211_Verwijtbaarheid.VERMINDERD_VERWIJTBAAR) {
                            yield "maatregel eerste categorie ter hoogte van 3% van het In aanmerking te nemen uitkeringsbedrag gedurende één uitkeringstermijn";
                        } else {
                            yield "maatregel eerste categorie ter hoogte van 7.5% van het In aanmerking te nemen uitkeringsbedrag gedurende één uitkeringstermijn";
                        }
                    }
                }
            }
            case D1213_Maatregel.SANCTIE_TWEEDE_CATEGORIE -> {
                boolean basisbedragIsLagerDanMinimum = getCalculatedValue(calculationContext, D12_BASISBEDRAG_IS_LAGER_DAN_MINIMUM_BEDRAG);
                D12131_RecidiveSoort recidive = getCalculatedValue(calculationContext, D12131_RECIDIVE);
                if (basisbedragIsLagerDanMinimum) {
                    if (recidive != D12131_RecidiveSoort.RECIDIVE_NA_MAATREGEL) {
                        yield "maatregel tweede categorie ter hoogte van het minimumbedrag van de maatregel gedurende twee uitkeringstermijnen";
                    } else {
                        yield "maatregel tweede categorie ter hoogte van anderhalf keer het minimumbedrag van de maatregel gedurende twee uitkeringstermijnen";
                    }
                } else {
                    D1211_Verwijtbaarheid mateVanVerwijtbaarheid = getCalculatedValue(calculationContext, D1211_MATE_VAN_VERWIJTBAARHEID);
                    if (recidive != D12131_RecidiveSoort.RECIDIVE_NA_MAATREGEL) {
                        if (mateVanVerwijtbaarheid == D1211_Verwijtbaarheid.VERMINDERD_VERWIJTBAAR) {
                            yield "maatregel tweede categorie ter hoogte van 5% van het In aanmerking te nemen uitkeringsbedrag gedurende twee uitkeringstermijnen";
                        } else {
                            yield "maatregel tweede categorie ter hoogte van 10% van het In aanmerking te nemen uitkeringsbedrag gedurende twee uitkeringstermijnen";
                        }
                    } else {
                        if (mateVanVerwijtbaarheid == D1211_Verwijtbaarheid.VERMINDERD_VERWIJTBAAR) {
                            yield "maatregel tweede categorie ter hoogte van 7.5% van het In aanmerking te nemen uitkeringsbedrag gedurende twee uitkeringstermijnen";
                        } else {
                            yield "maatregel tweede categorie ter hoogte van 15% van het In aanmerking te nemen uitkeringsbedrag gedurende twee uitkeringstermijnen";
                        }
                    }
                }
            }
        };
        return new Waarde<>(result, null);
    }
}
