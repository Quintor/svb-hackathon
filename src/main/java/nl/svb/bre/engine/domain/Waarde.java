package nl.svb.bre.engine.domain;

import nl.svb.bre.domain.GeldigheidsPeriode;

public record Waarde<T>(
        T value,
        GeldigheidsPeriode geldigheidsPeriode
) { }
