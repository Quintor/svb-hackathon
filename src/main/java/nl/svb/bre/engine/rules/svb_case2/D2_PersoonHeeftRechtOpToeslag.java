package nl.svb.bre.engine.rules.svb_case2;

import nl.svb.bre.domain.GeldigheidsPeriode;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import static nl.svb.bre.domain.enums.Definitiecode.D21_PERSOON_VOLDOET_AAN_DE_LEEFTIJDSEIS;
import static nl.svb.bre.domain.enums.Definitiecode.D22_PERSOON_VOLDOET_AAN_DE_INKOMENSEIS;
import static nl.svb.bre.domain.enums.Definitiecode.D23_PERSOON_IS_MINIMAAL_50_DAGEN_ACHTEREENVOLGENS_VERZEKERD_GEWEEST;
import static nl.svb.bre.domain.enums.Definitiecode.D2_PERSOON_HEEFT_RECHT_OP_TOESLAG;

@Component
public class D2_PersoonHeeftRechtOpToeslag extends Rule<Boolean> {

    @Override
    public Definitiecode getDefinitionCode() {
        return D2_PERSOON_HEEFT_RECHT_OP_TOESLAG;
    }

    @Override
    public DependencySet dependsOn() {
        return DependencySet.of(
                Dependency.of(D21_PERSOON_VOLDOET_AAN_DE_LEEFTIJDSEIS),
                Dependency.of(D22_PERSOON_VOLDOET_AAN_DE_INKOMENSEIS),
                Dependency.of(D23_PERSOON_IS_MINIMAAL_50_DAGEN_ACHTEREENVOLGENS_VERZEKERD_GEWEEST)
        );
    }

    @Override
    protected Waarde<Boolean> executeRule(CalculationContext calculationContext) {
        Boolean voldoetAanLeeftijdseis = getCalculatedValue(calculationContext, D21_PERSOON_VOLDOET_AAN_DE_LEEFTIJDSEIS);
        Boolean voldoetAanInkomenseis = getCalculatedValue(calculationContext, D22_PERSOON_VOLDOET_AAN_DE_INKOMENSEIS);
        Boolean voldoetAanVerzekerdeis = getCalculatedValue(calculationContext, D23_PERSOON_IS_MINIMAAL_50_DAGEN_ACHTEREENVOLGENS_VERZEKERD_GEWEEST);

        boolean heeftRecht = Boolean.TRUE.equals(voldoetAanLeeftijdseis)
                && Boolean.TRUE.equals(voldoetAanInkomenseis)
                && Boolean.TRUE.equals(voldoetAanVerzekerdeis);

        return new Waarde<>(heeftRecht, new GeldigheidsPeriode(calculationContext.getPeildatum(), calculationContext.getPeildatum()));
    }
}
