package nl.svb.bre.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.time.LocalDate;

@Embeddable
public record GeldigheidsPeriode(
        @Column(name = "startdatum") LocalDate start,
        @Column(name = "einddatum") LocalDate end
) {
}
