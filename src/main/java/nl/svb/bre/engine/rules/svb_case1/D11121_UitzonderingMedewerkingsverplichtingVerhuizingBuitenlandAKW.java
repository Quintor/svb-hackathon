package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.domain.enums.CalculationError;
import nl.svb.bre.engine.errors.FunctionalCalculationException;
import nl.svb.bre.engine.rules.Rule;

public class D11121_UitzonderingMedewerkingsverplichtingVerhuizingBuitenlandAKW extends Rule<Boolean> {
    @Override
    public Definitiecode getDefinitionCode() {
        return Definitiecode.D11121_UITZONDERING_MEDEWERKINGSVERPLICHTING_VERHUIZING_BUITENLAND_AKW;
    }

    @Override
    protected Waarde<Boolean> executeRule(CalculationContext calculationContext) {
        boolean isMeldplichtigeVerhuizingBuitenland = calculationContext.getTestObject().isMeldplichtigeVerhuizingBuitenland();
        if (!isMeldplichtigeVerhuizingBuitenland) {
            return new Waarde<>(false, null);
        }

        Boolean verhuizingTijdigGemeldNaVerzoek = calculationContext.getTestObject().verhuizingTijdigGemeldNaVerzoek();
        if (verhuizingTijdigGemeldNaVerzoek == null) {
            throw new FunctionalCalculationException(CalculationError.UNKNOWN_VALUE, "Unknown input: 'verhuizingTijdigGemeldNaVerzoek'");
        }
        return new Waarde<>(verhuizingTijdigGemeldNaVerzoek, null);
    }
}
