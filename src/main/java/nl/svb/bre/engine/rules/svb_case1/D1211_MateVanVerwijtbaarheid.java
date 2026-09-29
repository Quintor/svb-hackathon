package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import static nl.svb.bre.domain.enums.Definitiecode.D12111_DOOR_DE_EMOTIONELE_ONTWRICHTING_IS_DE_OVERTREDING_VERMINDERD_TOE_TE_REKENEN;
import static nl.svb.bre.domain.enums.Definitiecode.D12112_DOOR_DE_GEESTELIJKE_TOESTAND_IS_DE_OVERTREDING_VERMINDERD_TOE_TE_REKENEN;
import static nl.svb.bre.domain.enums.Definitiecode.D12113_VERMINDERD_TOE_TE_REKENEN;
import static nl.svb.bre.domain.enums.Definitiecode.D1211_MATE_VAN_VERWIJTBAARHEID;

@Component
public class D1211_MateVanVerwijtbaarheid extends Rule<Object> {
    @Override
    public Definitiecode getDefinitionCode() {
        return D1211_MATE_VAN_VERWIJTBAARHEID;
    }

    @Override
    public DependencySet dependsOn() {
        return DependencySet.of(
                Dependency.of(D12111_DOOR_DE_EMOTIONELE_ONTWRICHTING_IS_DE_OVERTREDING_VERMINDERD_TOE_TE_REKENEN),
                Dependency.of(D12112_DOOR_DE_GEESTELIJKE_TOESTAND_IS_DE_OVERTREDING_VERMINDERD_TOE_TE_REKENEN),
                Dependency.of(D12113_VERMINDERD_TOE_TE_REKENEN)

        );
    }

    @Override
    protected Waarde<Object> executeRule(CalculationContext calculationContext) {
        return null;
    }
}
