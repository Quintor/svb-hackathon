package nl.svb.bre.engine.domain;

import nl.svb.bre.domain.GeldigheidsPeriode;

public record Waarde<T>(
        T value,
        GeldigheidsPeriode geldigheidsPeriode

) {

    public static <T> Waarde<T> NIET_TE_BEPALEN() { // NOSONAR
        return new Waarde<>(null, null);
    }
}
