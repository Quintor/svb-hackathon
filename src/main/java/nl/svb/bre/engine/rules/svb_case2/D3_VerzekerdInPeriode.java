package nl.svb.bre.engine.rules.svb_case2;

import nl.svb.bre.domain.GeldigheidsPeriode;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.rules.Rule;
import nl.svb.bre.engine.utils.PeriodeUtil;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static nl.svb.bre.domain.enums.Definitiecode.D3_VERZEKERD_IN_PERIODE;

@Component
public class D3_VerzekerdInPeriode extends Rule<Boolean> {

    private static final GeldigheidsPeriode PERIODE_1 = new GeldigheidsPeriode(LocalDate.of(2025,1, 1), LocalDate.of(2025,12, 31));
    private static final GeldigheidsPeriode PERIODE_2 = new GeldigheidsPeriode(LocalDate.of(2026,1, 1), LocalDate.of(2026,1, 10));
    private static final GeldigheidsPeriode PERIODE_3 = new GeldigheidsPeriode(LocalDate.of(2026,1, 11), LocalDate.of(2026,10, 31));

    @Override
    public Definitiecode getDefinitionCode() {
        return D3_VERZEKERD_IN_PERIODE;
    }

    @Override
    protected Waarde<Boolean> executeRule(CalculationContext calculationContext) {
        if (PeriodeUtil.inPeriod(calculationContext.getPeildatum(), PERIODE_1)) {
            return new Waarde<>(true, PERIODE_1);
        } else if (PeriodeUtil.inPeriod(calculationContext.getPeildatum(), PERIODE_2)) {
            return new Waarde<>(false, PERIODE_2);
        } else {
            return new Waarde<>(true, PERIODE_3);
        }
    }
}
