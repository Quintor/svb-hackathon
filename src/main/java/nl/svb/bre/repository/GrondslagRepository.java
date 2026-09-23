package nl.svb.bre.repository;

import nl.svb.bre.domain.Grondslag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GrondslagRepository extends JpaRepository<Grondslag, Long> {
}
