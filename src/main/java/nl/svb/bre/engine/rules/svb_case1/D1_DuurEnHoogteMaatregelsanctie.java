package nl.svb.bre.engine.rules.svb_case1;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static nl.svb.bre.domain.enums.Definitiecode.D11_EISEN_MAATREGEL_SANCTIE;
import static nl.svb.bre.domain.enums.Definitiecode.D12_BASISBEDRAG_IS_LAGER_DAN_MINIMUM_BEDRAG;
import static nl.svb.bre.domain.enums.Definitiecode.D1_DUUR_HOOGTE_MAATREGEL;

@Component
public class D1_DuurEnHoogteMaatregelsanctie extends Rule<Map> {

    @Override
    public Definitiecode getDefinitionCode() {
        return D1_DUUR_HOOGTE_MAATREGEL;
    }

    @Override
    public DependencySet dependsOn() {
        return DependencySet.of(
                Dependency.of(D11_EISEN_MAATREGEL_SANCTIE),
                Dependency.of(D12_BASISBEDRAG_IS_LAGER_DAN_MINIMUM_BEDRAG)
        );
    }

    @Override
    protected Waarde<Map> executeRule(CalculationContext calculationContext) {
        return new Waarde<>(Map.of("Hoogte", "100", "Categorie", "A"), null);
    }
}
