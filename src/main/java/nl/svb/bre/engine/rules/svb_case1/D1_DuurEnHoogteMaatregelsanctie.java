package nl.svb.bre.engine.rules.svb_case1;

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
    private static final String RECIDIVE_NA_MAATREGEL = "recidive na maatregel";
    private static final String VERMINDERD_VERWIJTBAAR = "verminderd verwijtbaar";

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

        String typeMaatregelSanctie = getCalculatedValue(calculationContext, D1213_TYPE_MAATREGELSANCTIE);
        var result = switch (typeMaatregelSanctie) {
            case "maatregelwaarschuwing" -> "maatregelwaarschuwing";
            case "maatregel eerste categorie" -> {
                boolean basisbedragIsLagerDanMinimum = getCalculatedValue(calculationContext, D12_BASISBEDRAG_IS_LAGER_DAN_MINIMUM_BEDRAG);
                String recidive = getCalculatedValue(calculationContext, D12131_RECIDIVE);
                if (basisbedragIsLagerDanMinimum) {
                    if (!RECIDIVE_NA_MAATREGEL.equals(recidive)) {
                        yield "maatregel eerste categorie ter hoogte van het minimumbedrag van de maatregel gedurende één uitkeringstermijn";
                    } else {
                        yield "maatregel eerste categorie ter hoogte van anderhalf keer het minimumbedrag van de maatregel gedurende één uitkeringstermijn";
                    }
                } else {
                    String mateVanVerwijtbaarheid = getCalculatedValue(calculationContext, D1211_MATE_VAN_VERWIJTBAARHEID);
                    if (!RECIDIVE_NA_MAATREGEL.equals(recidive)) {
                        if (VERMINDERD_VERWIJTBAAR.equals(mateVanVerwijtbaarheid)) {
                            yield "maatregel eerste categorie ter hoogte van 2% van het In aanmerking te nemen uitkeringsbedrag gedurende één uitkeringstermijn";
                        } else {
                            yield "maatregel eerste categorie ter hoogte van 5% van het In aanmerking te nemen uitkeringsbedrag gedurende één uitkeringstermijn";
                        }
                    } else {
                        if (VERMINDERD_VERWIJTBAAR.equals(mateVanVerwijtbaarheid)) {
                            yield "maatregel eerste categorie ter hoogte van 3% van het In aanmerking te nemen uitkeringsbedrag gedurende één uitkeringstermijn";
                        } else {
                            yield "maatregel eerste categorie ter hoogte van 7.5% van het In aanmerking te nemen uitkeringsbedrag gedurende één uitkeringstermijn";
                        }
                    }
                }
            }
            case "maatregel tweede categorie" -> {
                boolean basisbedragIsLagerDanMinimum = getCalculatedValue(calculationContext, D12_BASISBEDRAG_IS_LAGER_DAN_MINIMUM_BEDRAG);
                String recidive = getCalculatedValue(calculationContext, D12131_RECIDIVE);
                if (basisbedragIsLagerDanMinimum) {
                    if (!RECIDIVE_NA_MAATREGEL.equals(recidive)) {
                        yield "maatregel tweede categorie ter hoogte van het minimumbedrag van de maatregel gedurende twee uitkeringstermijnen";
                    } else {
                        yield "maatregel tweede categorie ter hoogte van anderhalf keer het minimumbedrag van de maatregel gedurende twee uitkeringstermijnen";
                    }
                } else {
                    String mateVanVerwijtbaarheid = getCalculatedValue(calculationContext, D1211_MATE_VAN_VERWIJTBAARHEID);
                    if (!RECIDIVE_NA_MAATREGEL.equals(recidive)) {
                        if (VERMINDERD_VERWIJTBAAR.equals(mateVanVerwijtbaarheid)) {
                            yield "maatregel tweede categorie ter hoogte van 5% van het In aanmerking te nemen uitkeringsbedrag gedurende twee uitkeringstermijnen";
                        } else {
                            yield "maatregel tweede categorie ter hoogte van 10% van het In aanmerking te nemen uitkeringsbedrag gedurende twee uitkeringstermijnen";
                        }
                    } else {
                        if (VERMINDERD_VERWIJTBAAR.equals(mateVanVerwijtbaarheid)) {
                            yield "maatregel tweede categorie ter hoogte van 7.5% van het In aanmerking te nemen uitkeringsbedrag gedurende twee uitkeringstermijnen";
                        } else {
                            yield "maatregel tweede categorie ter hoogte van 15% van het In aanmerking te nemen uitkeringsbedrag gedurende twee uitkeringstermijnen";
                        }
                    }
                }
            }
            case null, default -> throw new FunctionalCalculationException(CalculationError.UNKNOWN_VALUE);
        };
        return new Waarde<>(result, null);
    }
}
