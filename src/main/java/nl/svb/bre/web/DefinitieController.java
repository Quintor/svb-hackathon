package nl.svb.bre.web;

import lombok.RequiredArgsConstructor;
import nl.svb.bre.domain.Definitie;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.repository.DefinitieRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DefinitieController {

    private final DefinitieRepository definitieRepository;

    @GetMapping("/definitiecodes")
    public List<Definitiecode> definitiecodes() {
        return definitieRepository.findAllByOrderByDefinitiecodeAsc().stream()
                .map(Definitie::getDefinitiecode)
                .distinct()
                .toList();
    }
}
