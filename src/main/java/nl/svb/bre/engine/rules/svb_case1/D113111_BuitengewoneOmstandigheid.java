package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import static nl.svb.bre.domain.enums.Definitiecode.D113111_BUITENGEWONE_OMSTANDIGHEID;

@Component
public class D113111_BuitengewoneOmstandigheid extends Rule<Boolean> {
    @Override
    public Definitiecode getDefinitionCode() {
        return D113111_BUITENGEWONE_OMSTANDIGHEID;
    }

    @Override
    protected Waarde<Boolean> executeRule(CalculationContext calculationContext) {
        return new Waarde<>(!(calculationContext.getTestObject().normaalLevenspatroonPersoon()
                || calculationContext.getTestObject().voorzieneOmstandigheid()
                || calculationContext.getTestObject().gewensteOmstandigheid()),
                null);
    }
}
