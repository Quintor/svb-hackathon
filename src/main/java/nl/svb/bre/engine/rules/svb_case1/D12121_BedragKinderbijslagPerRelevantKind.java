package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Component
public class D12121_BedragKinderbijslagPerRelevantKind extends Rule<Collection<BigDecimal>> {

    public final LocalDate datumV2 = LocalDate.of(2026, Month.APRIL, 1);

    private enum LeeftijdCat {
        NUL_TOT_ZES,
        ZES_TOT_TWAALF,
        TWAALF_TOT_ACHTIEN,
        ANDERS;

        public static LeeftijdCat getCategorie(Integer leeftijd) {
            if(leeftijd < 0 || leeftijd >=18) {
                return ANDERS;
            }
            if( leeftijd < 6 ) {
                return NUL_TOT_ZES;
            } else if( leeftijd < 12 ) {
                return ZES_TOT_TWAALF;
            }
            return TWAALF_TOT_ACHTIEN;
        }
    }

    public enum Woonland {
        IN_NL,
        BUITEN_NL
    }

    Map<LeeftijdCat, BigDecimal> bedragenV1 = Map.of(
                    LeeftijdCat.NUL_TOT_ZES, new BigDecimal("295.07"),
                    LeeftijdCat.ZES_TOT_TWAALF, new BigDecimal("358.30"),
                    LeeftijdCat.TWAALF_TOT_ACHTIEN, new BigDecimal("421.53"),
                    LeeftijdCat.ANDERS, BigDecimal.ZERO
    );

    Map<Woonland, Map<LeeftijdCat, BigDecimal>> bedragenV2 = Map.of(
            Woonland.IN_NL, Map.of(
                LeeftijdCat.NUL_TOT_ZES, new BigDecimal("300.00"),
                LeeftijdCat.ZES_TOT_TWAALF, new BigDecimal("370"),
                LeeftijdCat.TWAALF_TOT_ACHTIEN, new BigDecimal("450"),
                LeeftijdCat.ANDERS, BigDecimal.ZERO),
            Woonland.BUITEN_NL, Map.of(
                LeeftijdCat.NUL_TOT_ZES,  new BigDecimal("295.07"),
                LeeftijdCat.ZES_TOT_TWAALF, new BigDecimal("358.30"),
                LeeftijdCat.TWAALF_TOT_ACHTIEN, new BigDecimal("421.53"),
                LeeftijdCat.ANDERS, BigDecimal.ZERO)
    );

    @Override
    public Definitiecode getDefinitionCode() {
        return Definitiecode.D12121_BEDRAG_AKW;
    }

    @Override
    public DependencySet dependsOn() {
        return DependencySet.of(
                Dependency.of(Definitiecode.SVB_LEEFTIJD_KIND1),
                Dependency.of(Definitiecode.SVB_LEEFTIJD_KIND2),
                Dependency.of(Definitiecode.SVB_WOONLAND_KIND1),
                Dependency.of(Definitiecode.SVB_WOONLAND_KIND2)
        );
    }

    @Override
    protected Waarde<Collection<BigDecimal>> executeRule(CalculationContext calculationContext) {
        if (calculationContext.getPeildatum().isBefore(LocalDate.of(2026, Month.JANUARY, 1))) {
            return versionV1(calculationContext);
        } else {
            return versionV2(calculationContext);
        }
    }

    private Waarde<Collection<BigDecimal>> versionV1(CalculationContext calculationContext) {
        Integer leeftijd1 = getCalculatedValue(calculationContext, Definitiecode.SVB_LEEFTIJD_KIND1);
        Integer leeftijd2 = getCalculatedValue(calculationContext, Definitiecode.SVB_LEEFTIJD_KIND2);
        List<BigDecimal> list = new LinkedList<>();
        list.add(bedragLeeftijdV1(leeftijd1));
        list.add(bedragLeeftijdV1(leeftijd2));
        return new Waarde<>(list, null);
    }

    private BigDecimal bedragLeeftijdV1(Integer leeftijd) {
        LeeftijdCat categorie = LeeftijdCat.getCategorie(leeftijd);
        return bedragenV1.get(categorie);
    }

    private Waarde<Collection<BigDecimal>> versionV2(CalculationContext calculationContext) {
        List<BigDecimal> list = new LinkedList<>();
        Integer leeftijd = getCalculatedValue(calculationContext, Definitiecode.SVB_LEEFTIJD_KIND1);
        Woonland woonland = getCalculatedValue(calculationContext, Definitiecode.SVB_WOONLAND_KIND1);
        list.add(bedragLeeftijdV2(leeftijd, woonland));
        leeftijd = getCalculatedValue(calculationContext, Definitiecode.SVB_LEEFTIJD_KIND2);
        woonland = getCalculatedValue(calculationContext, Definitiecode.SVB_WOONLAND_KIND2);
        list.add(bedragLeeftijdV2(leeftijd, woonland));
        return new Waarde<>(list, null);
    }

    private BigDecimal bedragLeeftijdV2(Integer leeftijd, Woonland woonland) {
        LeeftijdCat categorie = LeeftijdCat.getCategorie(leeftijd);
        return bedragenV2.get(woonland).get(categorie);
    }

}
