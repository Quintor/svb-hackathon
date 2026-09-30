package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.D1211_Verwijtbaarheid;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import static java.util.function.Predicate.not;
import static nl.svb.bre.domain.enums.Definitiecode.D12111_DOOR_DE_EMOTIONELE_ONTWRICHTING_IS_DE_OVERTREDING_VERMINDERD_TOE_TE_REKENEN;
import static nl.svb.bre.domain.enums.Definitiecode.D12112_DOOR_DE_GEESTELIJKE_TOESTAND_IS_DE_OVERTREDING_VERMINDERD_TOE_TE_REKENEN;
import static nl.svb.bre.domain.enums.Definitiecode.D12113_VERMINDERD_TOE_TE_REKENEN;
import static nl.svb.bre.domain.enums.Definitiecode.D1211_MATE_VAN_VERWIJTBAARHEID;
import static nl.svb.bre.engine.utils.CalculationEnginePredicate.isD12111;
import static nl.svb.bre.engine.utils.CalculationEnginePredicate.isD12112;
import static nl.svb.bre.engine.utils.CalculationEnginePredicate.isOvertredingGedeeltelijkVerwijtbaar;

@Component
public class D1211_MateVanVerwijtbaarheid extends Rule<D1211_Verwijtbaarheid> {
    @Override
    public Definitiecode getDefinitionCode() {
        return D1211_MATE_VAN_VERWIJTBAARHEID;
    }

    @Override
    public DependencySet dependsOn() {
        return DependencySet.of(
                Dependency.of(D12111_DOOR_DE_EMOTIONELE_ONTWRICHTING_IS_DE_OVERTREDING_VERMINDERD_TOE_TE_REKENEN),
                Dependency.of(D12112_DOOR_DE_GEESTELIJKE_TOESTAND_IS_DE_OVERTREDING_VERMINDERD_TOE_TE_REKENEN, not(isD12111)),
                Dependency.of(D12113_VERMINDERD_TOE_TE_REKENEN, not(isD12111).and(not(isD12112)).and(not(isOvertredingGedeeltelijkVerwijtbaar)))
        );
    }

    @Override
    protected Waarde<D1211_Verwijtbaarheid> executeRule(CalculationContext calculationContext) {
        if(
                (Boolean)getCalculatedValue(calculationContext, D12111_DOOR_DE_EMOTIONELE_ONTWRICHTING_IS_DE_OVERTREDING_VERMINDERD_TOE_TE_REKENEN)
                || (Boolean)getCalculatedValue(calculationContext, D12112_DOOR_DE_GEESTELIJKE_TOESTAND_IS_DE_OVERTREDING_VERMINDERD_TOE_TE_REKENEN)
                || calculationContext.getTestObject().isOvertredingGedeeltelijkVerwijtbaar()
                || (Boolean)getCalculatedValue(calculationContext, D12113_VERMINDERD_TOE_TE_REKENEN)
                || calculationContext.getTestObject().isOvertredingMedeTeWijtenAanSvb()
        ){
            return new Waarde<>(D1211_Verwijtbaarheid.VERMINDERD_VERWIJTBAAR, null);
        }
        return new Waarde<>(D1211_Verwijtbaarheid.VOLLEDIG_VERWIJTBAAR, null);
    }
}
