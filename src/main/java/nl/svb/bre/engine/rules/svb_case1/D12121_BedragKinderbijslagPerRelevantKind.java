package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.Definitie;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

public class D12121_BedragKinderbijslagPerRelevantKind extends Rule<Collection<BigDecimal>> {

    BigDecimal nulTot6 = new BigDecimal("295.07");
    BigDecimal zesTot12 = new BigDecimal("358.30");
    BigDecimal twaalfTot18 = new BigDecimal("421.53");

    @Override
    public Definitiecode getDefinitionCode() {
        return Definitiecode.SVB_D12121_BEDRAG_AKW;
    }

    @Override
    public DependencySet dependsOn() {
        return DependencySet.of(
                Dependency.of(Definitiecode.SVB_LEEFTIJD_KIND1),
                Dependency.of(Definitiecode.SVB_LEEFTIJD_KIND2)
        );
    }

    @Override
    protected Waarde<Collection<BigDecimal>> executeRule(CalculationContext calculationContext) {
        Integer leeftijd1 = getCalculatedValue(calculationContext, Definitiecode.SVB_LEEFTIJD_KIND1);
        Integer leeftijd2 = getCalculatedValue(calculationContext, Definitiecode.SVB_LEEFTIJD_KIND2);
        List<BigDecimal> list = new LinkedList<>();
        list.add(bedragLeeftijd(leeftijd1));
        list.add(bedragLeeftijd(leeftijd2));
        return new Waarde<>(list, null);
    }

    private BigDecimal bedragLeeftijd(Integer leeftijd) {
        if(leeftijd < 0 || leeftijd >=18) {
            return new BigDecimal("0");
        }
        if( leeftijd < 6 ) {
            return nulTot6;
        } else if( leeftijd < 12 ) {
            return zesTot12;
        }
        return twaalfTot18;
    }
}
