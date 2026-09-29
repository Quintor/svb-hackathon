package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.D12131_RecidiveSoort;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.domain.enums.CalculationError;
import nl.svb.bre.engine.errors.FunctionalCalculationException;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import static nl.svb.bre.domain.enums.Definitiecode.D12131_RECIDIVE;

@Component
public class D12131_Recidive extends Rule<D12131_RecidiveSoort> {
    @Override
    public Definitiecode getDefinitionCode() {
        return D12131_RECIDIVE;
    }

    @Override
    protected Waarde<D12131_RecidiveSoort> executeRule(CalculationContext calculationContext) {
        String soortOvertreding = calculationContext.getTestObject().soortOvertredenVerplichting();
        return switch (soortOvertreding) {
            case "reageren op een informatieverzoek" -> informatieVerzoekOvertreding(calculationContext);
            case "nakomen verplichting tweede categorie" ->
                    throw new FunctionalCalculationException(CalculationError.MISSING_VALUE_D12131_A);
            case "nakomen overige controlevoorschriften" ->
                    throw new FunctionalCalculationException(CalculationError.MISSING_VALUE_D12131_B);
            default -> new Waarde<>(D12131_RecidiveSoort.GEEN_RECIDIVE, null);
        };
    }

    private Waarde<D12131_RecidiveSoort> informatieVerzoekOvertreding(CalculationContext calculationContext) {
        if (calculationContext.getTestObject().heeftRecidiveNietTijdigReagerenBinnenTweeJaar()) {
            return new Waarde<>(D12131_RecidiveSoort.RECIDIVE_NA_MAATREGEL, null);
        }
        if (calculationContext.getTestObject().heeftRecidiveNietReagerenBinnenTweeJaar() ){
            return new Waarde<>(D12131_RecidiveSoort.RECIDIVE_NA_MAATREGEL, null);
        }
        if (calculationContext.getTestObject().heeftMaatregelwaarschuwingBinnenTweeJaar()) {
            return new Waarde<>(D12131_RecidiveSoort.RECIDIVE_NA_MAATREGELWAARSCHUWING, null);
        }
        return new Waarde<>(D12131_RecidiveSoort.GEEN_RECIDIVE, null);
    }
}
