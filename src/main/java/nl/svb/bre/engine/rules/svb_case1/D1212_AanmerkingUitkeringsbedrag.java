package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Collection;

@Component
public class D1212_AanmerkingUitkeringsbedrag extends Rule<BigDecimal> {

    @Override
    public Definitiecode getDefinitionCode() {
        return Definitiecode.D1212_AANMERKING_UITKERINGSBEDRAG;
    }

    @Override
    public DependencySet dependsOn() {
        return DependencySet.of(
                Dependency.of(Definitiecode.D12121_BEDRAG_AKW)
        );
    }

    @Override
    protected Waarde<BigDecimal> executeRule(CalculationContext calculationContext) {
        Collection<Waarde<BigDecimal>> bedragen = (Collection<Waarde<BigDecimal>>) getCalculatedValue(calculationContext, Definitiecode.D12121_BEDRAG_AKW);
        return new Waarde<>(bedragen.stream()
                .map(Waarde::value)
                .reduce(BigDecimal.ZERO, BigDecimal::add), null);
    }
}
