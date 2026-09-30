package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.domain.enums.CalculationError;
import nl.svb.bre.engine.errors.FunctionalCalculationException;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import java.util.function.Predicate;

import static nl.svb.bre.domain.enums.Definitiecode.D111_MEDEWERKINGSVERPLICHTING_OVERTREDEN;
import static nl.svb.bre.domain.enums.Definitiecode.D112_UITKERING_WAARDE_OP_MAATREGEL_SANCTIE;
import static nl.svb.bre.domain.enums.Definitiecode.D113_SVB_ZIET_AF_VAN_HET_OPLEGGEN_VAN_EEN_MAATREGEL;
import static nl.svb.bre.domain.enums.Definitiecode.D11_EISEN_MAATREGEL_SANCTIE;
import static nl.svb.bre.engine.utils.CalculationEnginePredicate.isInstelling;
import static nl.svb.bre.engine.utils.CalculationEnginePredicate.medewerkingsverplichtingAkwOvertreden;
import static nl.svb.bre.engine.utils.CalculationEnginePredicate.uitkeringswaardeBijMaatregel;

@Component
public class D11_EisenMaatregelsanctie extends Rule<Boolean> {

    @Override
    public Definitiecode getDefinitionCode() {
        return D11_EISEN_MAATREGEL_SANCTIE;
    }

    @Override
    public DependencySet dependsOn() {
        return DependencySet.of(
                Dependency.of(D111_MEDEWERKINGSVERPLICHTING_OVERTREDEN),
                Dependency.of(D112_UITKERING_WAARDE_OP_MAATREGEL_SANCTIE, medewerkingsverplichtingAkwOvertreden.and(Predicate.not(isInstelling))),
                Dependency.of(D113_SVB_ZIET_AF_VAN_HET_OPLEGGEN_VAN_EEN_MAATREGEL, medewerkingsverplichtingAkwOvertreden.and(Predicate.not(isInstelling.or(uitkeringswaardeBijMaatregel))))
        );
    }

    @Override
    protected Waarde<Boolean> executeRule(CalculationContext calculationContext) {
        Boolean medewerkingsverplichtingOvertreden = getCalculatedValue(calculationContext, D111_MEDEWERKINGSVERPLICHTING_OVERTREDEN);
        if (medewerkingsverplichtingOvertreden == null) {
            return Waarde.NIET_TE_BEPALEN();
        } else if (!medewerkingsverplichtingOvertreden) {
            return new Waarde<>(false, null);
        }

        var isInstelling = calculationContext.getTestObject().isInstelling();
        if (isInstelling == null) {
            throw new FunctionalCalculationException(CalculationError.UNKNOWN_VALUE);
        } else if (isInstelling) {
            return new Waarde<>(false, null);
        }

        Boolean uitkeringWaardeOpMaatregelSanctie = getCalculatedValue(calculationContext, D112_UITKERING_WAARDE_OP_MAATREGEL_SANCTIE);
        if (uitkeringWaardeOpMaatregelSanctie == null) {
            return Waarde.NIET_TE_BEPALEN();
        } else if (uitkeringWaardeOpMaatregelSanctie) {
            return new Waarde<>(false, null);
        }

        Boolean svbZietAfVanHetOpleggenVanEenMaatregel = getCalculatedValue(calculationContext, D113_SVB_ZIET_AF_VAN_HET_OPLEGGEN_VAN_EEN_MAATREGEL);
        if (svbZietAfVanHetOpleggenVanEenMaatregel == null) {
            return Waarde.NIET_TE_BEPALEN();
        }
        return new Waarde<>(!svbZietAfVanHetOpleggenVanEenMaatregel, null);
    }
}
