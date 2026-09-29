package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

import static nl.svb.bre.domain.enums.Definitiecode.D1131_PERSOON_VERWIJTBAAR;
import static nl.svb.bre.domain.enums.Definitiecode.D113_SVB_ZIET_AF_VAN_HET_OPLEGGEN_VAN_EEN_MAATREGEL;
import static nl.svb.bre.engine.utils.CalculationEnginePredicate.heeftSvbBoeteVoorZelfdeGedraging;
import static nl.svb.bre.engine.utils.CalculationEnginePredicate.heeftSvbBoetewaarschuwingVoorZelfdeGedraging;

@Component
public class D113_SVBZietAfVanHetOpleggenVanEenMaatregel extends Rule<Boolean> {
    @Override
    public Definitiecode getDefinitionCode() {
        return D113_SVB_ZIET_AF_VAN_HET_OPLEGGEN_VAN_EEN_MAATREGEL;
    }

    @Override
    public DependencySet dependsOn() {
        return DependencySet.of(
                Dependency.of(D1131_PERSOON_VERWIJTBAAR, Predicate
                        .not(heeftSvbBoetewaarschuwingVoorZelfdeGedraging
                                .or(heeftSvbBoeteVoorZelfdeGedraging)))
        );
    }

    @Override
    protected Waarde<Boolean> executeRule(CalculationContext calculationContext) {
        return new Waarde<>(calculationContext.getTestObject().heeftSvbBoetewaarschuwingVoorZelfdeGedraging()
                || calculationContext.getTestObject().heeftSvbBoeteVoorZelfdeGedraging()
                || !this.<Boolean>getCalculatedValue(calculationContext, D1131_PERSOON_VERWIJTBAAR)
                || calculationContext.getTestObject().isDringendeRedenAanwezig(),
                null);
    }
}
