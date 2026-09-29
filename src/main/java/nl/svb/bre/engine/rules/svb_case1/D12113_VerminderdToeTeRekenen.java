package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.domain.enums.CalculationError;
import nl.svb.bre.engine.errors.FunctionalCalculationException;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import static nl.svb.bre.domain.enums.Definitiecode.D12113_VERMINDERD_TOE_TE_REKENEN;

@Component
public class D12113_VerminderdToeTeRekenen extends Rule<Boolean> {
    @Override
    public Definitiecode getDefinitionCode() {
        return D12113_VERMINDERD_TOE_TE_REKENEN;
    }

    @Override
    protected Waarde<Boolean> executeRule(CalculationContext calculationContext) {
        boolean b1 = calculationContext.getTestObject().heeftOnjuisteInlichtingenTijdigHersteld();
        Boolean b2 = null;
        Boolean b3 = null;
        boolean b4 = calculationContext.getTestObject().heeftInlichtingenVerstrektBijSvbControle();

        if( b1 ) {
            return new Waarde<>(!b4, null);
        } else {
            if (b2==null) {
                throw new FunctionalCalculationException(CalculationError.MISSING_VALUE_D12113);
            }
            // implement the rest of the beslissings tabel
        }

        return null;
    }
}
