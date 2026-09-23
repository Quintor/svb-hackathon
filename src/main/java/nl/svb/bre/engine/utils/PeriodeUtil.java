package nl.svb.bre.engine.utils;

import nl.svb.bre.domain.GeldigheidsPeriode;

import java.time.LocalDate;

public final class PeriodeUtil {
    private PeriodeUtil() {
    }

    public static boolean inPeriod(LocalDate date, GeldigheidsPeriode periode) {
        return date.isAfter(periode.start()) && date.isBefore(periode.end());
    }
}
