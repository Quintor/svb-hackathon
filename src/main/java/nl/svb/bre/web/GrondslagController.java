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
                                @RequestParam final Integer distance,
                                @RequestParam final Boolean electric,
                                @RequestParam final ExampleVehicle vehicle,
                                @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate peildatum) {
        return treeCalculator.calculateResult(definitiecode, new TestObject(distance, electric, vehicle), peildatum);
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
