package nl.svb.bre.engine.rules;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Waarde;

import java.time.LocalDate;

public class BerekenLeeftijd1 extends Rule<Integer> {

    @Override
    public Definitiecode getDefinitionCode() {
        return Definitiecode.SVB_LEEFTIJD_KIND1;
    }

    @Override
    protected Waarde<Integer> executeRule(CalculationContext calculationContext) {
        LocalDate geboorteDatum =  calculationContext.getTestObject().geboortedatumKind1();
        LocalDate pijlDatum = calculationContext.getPeildatum();
        geboorteDatum = geboorteDatum.minusDays(geboorteDatum.getDayOfMonth()+1);
        pijlDatum = pijlDatum.minusDays(pijlDatum.getDayOfMonth()+1);

        if(pijlDatum.getDayOfYear() < geboorteDatum.getDayOfYear()) {
            return new Waarde<>(pijlDatum.minusYears(geboorteDatum.getYear()).getYear()+1, null);
        }
        return new Waarde<>(pijlDatum.minusYears(geboorteDatum.getYear()).getYear(), null);
    }
}
