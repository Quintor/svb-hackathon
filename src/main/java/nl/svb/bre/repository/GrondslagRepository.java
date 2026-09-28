package nl.svb.bre.repository;

import nl.svb.bre.domain.Grondslag;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrondslagRepository extends JpaRepository<Grondslag, Long> {

    @EntityGraph(attributePaths = {"grondslaggegevens", "grondslaggegevens.definitie"})
    Grondslag findByPersoonId(Long persoonId);
}
