package nl.svb.bre.engine.rules;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Waarde;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class BerekenLeeftijd2 extends Rule<Integer> {

    @Override
    public Definitiecode getDefinitionCode() {
        return Definitiecode.SVB_LEEFTIJD_KIND2;
    }

    @Override
    protected Waarde<Integer> executeRule(CalculationContext calculationContext) {
        LocalDate geboorteDatum =  calculationContext.getTestObject().geboortedatumKind2();
        LocalDate pijlDatum = calculationContext.getPeildatum();
        pijlDatum = pijlDatum.minusDays(pijlDatum.getDayOfMonth()+1);
        if(pijlDatum.getDayOfYear() < geboorteDatum.getDayOfYear()) {
            return new Waarde<>(pijlDatum.minusYears(geboorteDatum.getYear()).getYear()+1, null);
        }
        return new Waarde<>(pijlDatum.minusYears(geboorteDatum.getYear()).getYear(), null);
    }
}
