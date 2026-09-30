package nl.svb.bre.engine.rules.svb_case1;

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

import java.util.function.Predicate;

import static nl.svb.bre.domain.enums.D12131_RecidiveSoort.RECIDIVE_NA_MAATREGEL;
import static nl.svb.bre.domain.enums.D12131_RecidiveSoort.RECIDIVE_NA_MAATREGELWAARSCHUWING;
import static nl.svb.bre.domain.enums.Definitiecode.D12131_RECIDIVE;
import static nl.svb.bre.domain.enums.Definitiecode.D1213_TYPE_MAATREGELSANCTIE;

@Component
public class D1213_TypeMaatregelsanctie extends Rule<D1213_Maatregel> {

    private static final Predicate<CalculationContext> soortOvertredenVerplichtingIsReagerenOpEenInformatieverzoek = c -> "reageren op een informatieverzoek".equalsIgnoreCase(c.getTestObject().soortOvertredenVerplichting());

    @Override
    public Definitiecode getDefinitionCode() {
        return D1213_TYPE_MAATREGELSANCTIE;
    }

    @Override
    public DependencySet dependsOn() {
        return DependencySet.of(
                Dependency.of(D12131_RECIDIVE, soortOvertredenVerplichtingIsReagerenOpEenInformatieverzoek)
        );
    }

    @Override
    protected Waarde<D1213_Maatregel> executeRule(CalculationContext calculationContext) {
        String soortOvertreding = calculationContext.getTestObject().soortOvertredenVerplichting();
        return switch (soortOvertreding) {
            case "reageren op een informatieverzoek" -> informatieVerzoek(calculationContext);
            case "nakomen verplichting tweede categorie" ->
                    new Waarde<>(D1213_Maatregel.SANCTIE_TWEEDE_CATEGORIE, null);
            case "nakomen overige controlevoorschriften" ->
                    new Waarde<>(D1213_Maatregel.SANCTIE_EERSTE_CATEGORIE, null);
            case null, default -> new Waarde<>(null, null);
        };
    }

    private Waarde<D1213_Maatregel> informatieVerzoek(CalculationContext calculationContext) {
        D12131_RecidiveSoort recidiveSoort = getCalculatedValue(calculationContext, D12131_RECIDIVE);

        if (recidiveSoort.equals(RECIDIVE_NA_MAATREGEL) || recidiveSoort.equals(RECIDIVE_NA_MAATREGELWAARSCHUWING)) {
            return new Waarde<>(D1213_Maatregel.SANCTIE_EERSTE_CATEGORIE, null);
        }

        if (calculationContext.getTestObject().schorsingsbeslissingGenomen()) {
            return new Waarde<>(D1213_Maatregel.SANCTIE_EERSTE_CATEGORIE, null);
        }

        if (calculationContext.getTestObject().heeftOvertredingBenadelingsbedrag()) {
            return new Waarde<>(D1213_Maatregel.SANCTIE_EERSTE_CATEGORIE, null);
        }

        return new Waarde<>(D1213_Maatregel.WAARSCHUWING, null);
    }
}
