package nl.svb.bre.web;

import lombok.RequiredArgsConstructor;
import nl.svb.bre.domain.Grondslag;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.TreeCalculator;
import nl.svb.bre.engine.domain.TestObject;
import nl.svb.bre.engine.domain.enums.ExampleVehicle;
import nl.svb.bre.repository.GrondslagRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class GrondslagController {

    private final TreeCalculator treeCalculator;
    private final GrondslagRepository grondslagRepository;

    @GetMapping("/calculate")
    public Grondslag calculate(@RequestParam final Definitiecode definitiecode,
                                @RequestParam final Long persoonId,
                                @RequestParam final Long persoonIdKind1,
                                @RequestParam final Long persoonIdKind2,
                                @RequestParam final Integer testgeval,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate peildatum) {
        return treeCalculator.calculateResult(definitiecode, new TestObject(
                persoonId,
                List.of(persoonIdKind1, persoonIdKind2),
                true,
                false,
                false,
                "andere reden",
                false,
                false,
                true,
                true,
                "reageren op een informatieverzoek",
                false,
                false,
                false,
                true,
                false,
                false,
                false,
                null,
                false,
                false,
                false,
                "Een wijziging in het adres van de niet in de BRP ingeschreven kinderbijslaggerechtigde onverwijld melden",
                true,
                false,
                false,
                false,
                false,
                false,
                false,
                LocalDate.of(2016, 3,25),
                LocalDate.of(2018, 4,1)
        ), peildatum);
    }

    @GetMapping("/grondslag")
    public List<Grondslag> list() {
        return grondslagRepository.findAll();
    }

    @GetMapping("/grondslag/{id}")
    public ResponseEntity<Grondslag> getById(@PathVariable final Long id) {
        return grondslagRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
