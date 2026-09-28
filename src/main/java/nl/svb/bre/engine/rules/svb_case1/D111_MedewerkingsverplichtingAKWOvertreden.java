package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

@Component
public class D111_MedewerkingsverplichtingAKWOvertreden extends Rule<Boolean> {

    @Override
    public Definitiecode getDefinitionCode() {
        return Definitiecode.D111_MEDEWERKINGSVERPLICHTING_OVERTREDEN;
    }

    @Override
    protected Waarde<Boolean> executeRule(CalculationContext calculationContext) {
        return new Waarde(true, null);
    }
}
