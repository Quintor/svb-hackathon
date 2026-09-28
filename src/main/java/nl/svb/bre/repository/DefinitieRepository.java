package nl.svb.bre.repository;

import nl.svb.bre.domain.Definitie;
import nl.svb.bre.domain.enums.Definitiecode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DefinitieRepository extends JpaRepository<Definitie, Long> {

    Definitie findByDefinitiecode(final Definitiecode definitiecode);

    List<Definitie> findAllByOrderByDefinitiecodeAsc();
}
