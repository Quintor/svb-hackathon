package nl.svb.bre.engine;

import nl.svb.bre.domain.Grondslag;
import nl.svb.bre.domain.Grondslaggegeven;
import nl.svb.bre.engine.domain.ExampleObject;
import nl.svb.bre.engine.domain.TestObject;
import nl.svb.bre.engine.domain.enums.ExampleVehicle;
import nl.svb.bre.domain.enums.Definitiecode;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@SpringBootTest
class TreeCalculatorIntegrationTest {

    @Autowired
    private TreeCalculator treeCalculator;

    private static Stream<Arguments> calculateResults() {
        return Stream.of(
                Arguments.of(18, false, "PT1H"),
                Arguments.of(100, false, "PT5H33M"),
                Arguments.of(22, true, "PT1H"),
                Arguments.of(100, true, "PT5H2M"),
                Arguments.of(200, true, "PT10H5M")
        );
    }

    @ParameterizedTest
    @MethodSource
    void calculateResults(Integer distance, boolean electric, String duration) {
        Grondslag outcome = treeCalculator.calculateResult(Definitiecode.EXAMPLE_JOURNEY, new ExampleObject(distance, electric, ExampleVehicle.BICYCLE), LocalDate.of(2024, 1, 1));
        String result = findGrondslaggegevenByDefinitiecode(outcome, Definitiecode.EXAMPLE_JOURNEY).getWaarde();
        assertThat(result, is("The journey by bicycle will take " + duration));
    }

    private Grondslaggegeven<?> findGrondslaggegevenByDefinitiecode(Grondslag grondslag, Definitiecode definitiecode) {
        return grondslag.getGrondslaggegevens().stream()
                .filter(grondslaggegeven -> grondslaggegeven.getDefinitie().getDefinitiecode() == definitiecode)
                .findAny().orElseThrow();

    }
}
