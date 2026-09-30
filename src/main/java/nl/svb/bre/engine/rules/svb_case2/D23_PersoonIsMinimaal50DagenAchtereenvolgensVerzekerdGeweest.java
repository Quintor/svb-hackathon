package nl.svb.bre.engine.rules.svb_case2;

import nl.svb.bre.domain.GeldigheidsPeriode;
import nl.svb.bre.domain.Grondslaggegeven;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import nl.svb.bre.engine.utils.PeriodeUtil;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static java.time.temporal.ChronoUnit.DAYS;

@Component
public class D23_PersoonIsMinimaal50DagenAchtereenvolgensVerzekerdGeweest extends Rule<Boolean> {
    @Override
    public Definitiecode getDefinitionCode() {
        return Definitiecode.D23_PERSOON_IS_MINIMAAL_50_DAGEN_ACHTEREENVOLGENS_VERZEKERD_GEWEEST;
    }

    @Override
    protected Waarde<Boolean> executeRule(CalculationContext calculationContext) {
        var peildatum = calculationContext.getPeildatum();
        var geldigheidsPeriode = new GeldigheidsPeriode(calculationContext.getPeildatum(), calculationContext.getPeildatum());

        Optional<Grondslaggegeven<?>> gg = calculationContext.getGrondslag().getGrondslaggegevens()
                .stream().filter(grondslaggegeven -> grondslaggegeven.getDefinitie().getDefinitiecode() == Definitiecode.D3_VERZEKERD_IN_PERIODE)
                .filter(grondslaggegeven -> PeriodeUtil.inPeriod(peildatum, grondslaggegeven.getGeldigheidsPeriode()))
                .findFirst();

        if (gg.isEmpty()) {
            return new Waarde<>(false, geldigheidsPeriode);
        }

        var daysBetween = DAYS.between(gg.get().getGeldigheidsPeriode().start(), peildatum);
        return new Waarde<>(daysBetween > 50L, geldigheidsPeriode);
    }
}
