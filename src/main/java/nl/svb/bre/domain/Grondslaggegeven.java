package nl.svb.bre.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Grondslaggegeven<T> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Long id;

    @ManyToOne
    private Definitie definitie;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @EqualsAndHashCode.Exclude
    private Set<Grondslaggegeven<?>> onderliggend = new HashSet<>();

    private GeldigheidsPeriode geldigheidsPeriode;

    private String waarde;
}
