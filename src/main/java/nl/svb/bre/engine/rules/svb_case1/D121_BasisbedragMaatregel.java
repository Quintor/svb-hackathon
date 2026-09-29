package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.D1211_Verwijtbaarheid;
import nl.svb.bre.domain.enums.D1213_Maatregel;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.domain.enums.CalculationError;
import nl.svb.bre.engine.errors.FunctionalCalculationException;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

import static nl.svb.bre.domain.enums.Definitiecode.D1211_MATE_VAN_VERWIJTBAARHEID;
import static nl.svb.bre.domain.enums.Definitiecode.D1212_AANMERKING_UITKERINGSBEDRAG;
import static nl.svb.bre.domain.enums.Definitiecode.D1213_TYPE_MAATREGELSANCTIE;
import static nl.svb.bre.domain.enums.Definitiecode.D121_BASISBEDRAG_MAATREGEL;

@Component
public class D121_BasisbedragMaatregel extends Rule<BigDecimal> {
    @Override
    public Definitiecode getDefinitionCode() {
        return D121_BASISBEDRAG_MAATREGEL;
    }

    @Override
    public DependencySet dependsOn() {
        return DependencySet.of(
                Dependency.of(D1211_MATE_VAN_VERWIJTBAARHEID),
                Dependency.of(D1212_AANMERKING_UITKERINGSBEDRAG),
                Dependency.of(D1213_TYPE_MAATREGELSANCTIE)
        );
    }

    @Override
    protected Waarde<BigDecimal> executeRule(CalculationContext calculationContext) {
        D1211_Verwijtbaarheid verwijtbaarheid = getCalculatedValue(calculationContext, D1211_MATE_VAN_VERWIJTBAARHEID);
        BigDecimal uitkeringsbedrag = getCalculatedValue(calculationContext, D1212_AANMERKING_UITKERINGSBEDRAG);
        D1213_Maatregel maatregel = getCalculatedValue(calculationContext, D1213_TYPE_MAATREGELSANCTIE);

        return switch(maatregel) {
            case WAARSCHUWING -> new Waarde<>(BigDecimal.ZERO, null);
            case D1213_Maatregel.SANCTIE_EERSTE_CATEGORIE -> maatregelEersteCategorie(verwijtbaarheid, uitkeringsbedrag);
            case D1213_Maatregel.SANCTIE_TWEEDE_CATEGORIE -> maatregelTweedeCategorie(verwijtbaarheid, uitkeringsbedrag);
        };
    }

    private Waarde<BigDecimal> maatregelEersteCategorie(D1211_Verwijtbaarheid verwijtbaarheid, BigDecimal uitkeringsbedrag) {
        return switch(verwijtbaarheid) {
            case VOLLEDIG_VERWIJTBAAR -> new Waarde<>(uitkeringsbedrag.multiply(new BigDecimal("0.05")), null);
            case VERMINDERD_VERWIJTBAAR -> new Waarde<>(uitkeringsbedrag.multiply(new BigDecimal("0.02")), null);
        };
    }

    private Waarde<BigDecimal> maatregelTweedeCategorie(D1211_Verwijtbaarheid verwijtbaarheid, BigDecimal uitkeringsbedrag) {
        return switch(verwijtbaarheid) {
            case VOLLEDIG_VERWIJTBAAR -> new Waarde<>(uitkeringsbedrag.multiply(new BigDecimal("0.10")), null);
            case VERMINDERD_VERWIJTBAAR -> new Waarde<>(uitkeringsbedrag.multiply(new BigDecimal("0.05")), null);
        };
    }
}
