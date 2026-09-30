package nl.svb.bre.engine;

import nl.svb.bre.domain.Definitie;
import nl.svb.bre.domain.Grondslag;
import nl.svb.bre.domain.Grondslaggegeven;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.domain.ExampleObject;
import nl.svb.bre.engine.domain.TestObject;
import nl.svb.bre.engine.domain.Waarde;
import nl.svb.bre.engine.domain.enums.CalculationError;
import nl.svb.bre.engine.domain.enums.ExampleVehicle;
import nl.svb.bre.engine.errors.FunctionalCalculationException;
import nl.svb.bre.engine.rules.Rule;
import nl.svb.bre.repository.DefinitieRepository;
import nl.svb.bre.repository.GrondslagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

import static nl.svb.bre.domain.enums.Definitiecode.EXAMPLE_DISTANCE;
import static nl.svb.bre.domain.enums.Definitiecode.EXAMPLE_JOURNEY;
import static nl.svb.bre.domain.enums.Definitiecode.EXAMPLE_JOURNEY_BICYCLE;
import static nl.svb.bre.domain.enums.Definitiecode.EXAMPLE_VEHICLE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TreeCalculatorTest {

    @Mock
    private GrondslagRepository grondslagRepository;
    @Mock
    private DefinitieRepository definitieRepository;

    private final Map<Definitiecode, Rule<?>> calculationRules = new HashMap<>();
    private TreeCalculator treeCalculator;
    private ExampleObject testObject;

    @BeforeEach
    void setUp() {
        treeCalculator = new TreeCalculator(grondslagRepository, definitieRepository, calculationRules);
        testObject = new ExampleObject(100, false, ExampleVehicle.BICYCLE);
        when(grondslagRepository.save(any(Grondslag.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    @Test
    void calculateResult_noDependencies_persistsGrondslagWithOneGrondslaggegeven() {
        calculationRules.put(EXAMPLE_VEHICLE, stubRule(EXAMPLE_VEHICLE, DependencySet.of(), context -> ExampleVehicle.BICYCLE));
        Definitie definitie = definitie(EXAMPLE_VEHICLE);
        when(definitieRepository.findByDefinitiecode(EXAMPLE_VEHICLE)).thenReturn(definitie);

        Grondslag grondslag = treeCalculator.calculateResult(EXAMPLE_VEHICLE, testObject, LocalDate.of(2024, 1, 1));

        assertThat(grondslag.getGrondslaggegevens(), hasSize(1));
        Grondslaggegeven<?> gegeven = findByDefinitiecode(grondslag, EXAMPLE_VEHICLE);
        assertThat(gegeven.getDefinitie(), is(definitie));
        assertThat(gegeven.getWaarde(), is("BICYCLE"));
        assertThat(gegeven.getOnderliggend(), is(empty()));
        verify(grondslagRepository).save(grondslag);
    }

    @Test
    void calculateResults_withDependency_linksOnderliggendAndExecutesDependencyFirst() {
        java.util.List<Definitiecode> executionOrder = new java.util.LinkedList<>();
        Rule<?> vehicleRule = stubRule(EXAMPLE_VEHICLE, DependencySet.of(), context -> {
            executionOrder.add(EXAMPLE_VEHICLE);
            return ExampleVehicle.BICYCLE;
        });
        Rule<?> journeyRule = stubRule(EXAMPLE_JOURNEY, DependencySet.of(Dependency.of(EXAMPLE_VEHICLE)), context -> {
            executionOrder.add(EXAMPLE_JOURNEY);
            return "done";
        });
        calculationRules.put(EXAMPLE_VEHICLE, vehicleRule);
        calculationRules.put(EXAMPLE_JOURNEY, journeyRule);
        when(definitieRepository.findByDefinitiecode(EXAMPLE_VEHICLE)).thenReturn(definitie(EXAMPLE_VEHICLE));
        when(definitieRepository.findByDefinitiecode(EXAMPLE_JOURNEY)).thenReturn(definitie(EXAMPLE_JOURNEY));

        Grondslag grondslag = treeCalculator.calculateResult(EXAMPLE_JOURNEY, testObject, LocalDate.of(2024, 1, 1));

        assertThat(executionOrder, contains(EXAMPLE_VEHICLE, EXAMPLE_JOURNEY));
        Grondslaggegeven<?> journeyGegeven = findByDefinitiecode(grondslag, EXAMPLE_JOURNEY);
        Grondslaggegeven<?> vehicleGegeven = findByDefinitiecode(grondslag, EXAMPLE_VEHICLE);
        assertThat(journeyGegeven.getOnderliggend(), contains(vehicleGegeven));
        assertThat(vehicleGegeven.getOnderliggend(), is(empty()));
    }

    @Test
    void calculateResults_conditionalDependencyNotMet_dependencyNotExecuted() {
        Predicate<CalculationContext> neverMet = context -> false;
        Rule<?> bicycleRule = mock(Rule.class);
        Rule<?> journeyRule = stubRule(EXAMPLE_JOURNEY, DependencySet.of(Dependency.of(EXAMPLE_JOURNEY_BICYCLE, neverMet)), context -> "car");
        calculationRules.put(EXAMPLE_JOURNEY_BICYCLE, bicycleRule);
        calculationRules.put(EXAMPLE_JOURNEY, journeyRule);
        when(definitieRepository.findByDefinitiecode(EXAMPLE_JOURNEY)).thenReturn(definitie(EXAMPLE_JOURNEY));

        Grondslag grondslag = treeCalculator.calculateResult(EXAMPLE_JOURNEY, testObject, LocalDate.of(2024, 1, 1));

        verify(bicycleRule, never()).execute(any());
        assertThat(findByDefinitiecode(grondslag, EXAMPLE_JOURNEY).getOnderliggend(), is(empty()));
    }

    @Test
    void calculateResults_sharedDependency_isExecutedOnceButOnlyLinkedToFirstConsumer() {
        Rule<?> vehicleRule = stubRule(EXAMPLE_VEHICLE, DependencySet.of(), context -> ExampleVehicle.BICYCLE);
        Rule<?> journeyRule = stubRule(EXAMPLE_JOURNEY, DependencySet.of(Dependency.of(EXAMPLE_VEHICLE)), context -> "journey");
        Rule<?> distanceRule = stubRule(EXAMPLE_DISTANCE, DependencySet.of(Dependency.of(EXAMPLE_VEHICLE)), context -> 10);
        calculationRules.put(EXAMPLE_VEHICLE, vehicleRule);
        calculationRules.put(EXAMPLE_JOURNEY, journeyRule);
        calculationRules.put(EXAMPLE_DISTANCE, distanceRule);
        when(definitieRepository.findByDefinitiecode(any())).thenAnswer(invocation -> definitie(invocation.getArgument(0)));

        treeCalculator.calculateResults(Set.of(EXAMPLE_JOURNEY, EXAMPLE_DISTANCE), testObject, LocalDate.of(2024, 1, 1));

        verify(vehicleRule, times(1)).execute(any());
    }

    @Test
    void calculateResults_ruleThrowsFunctionalException_isCaughtAndGrondslagStillPersisted() {
        Rule<?> failingRule = mock(Rule.class);
        when(failingRule.dependsOn()).thenReturn(DependencySet.of());
        when(failingRule.execute(any())).thenThrow(new FunctionalCalculationException(CalculationError.UNKNOWN_VEHICLE));
        calculationRules.put(EXAMPLE_JOURNEY, failingRule);

        Grondslag grondslag = treeCalculator.calculateResult(EXAMPLE_JOURNEY, testObject, LocalDate.of(2024, 1, 1));

        assertThat(grondslag.getGrondslaggegevens(), is(hasSize(1)));
        verify(grondslagRepository).save(grondslag);
    }

    @Test
    void calculateResults_existingGrondslaggegevenRuleNowFails_clearsPreviousWaarde() {
        var existing = new Grondslaggegeven<>(1L, definitie(EXAMPLE_JOURNEY), new HashSet<>(), null, "old value", null);
        var stored = new Grondslag(1L, null, new HashSet<>(Set.of(existing)));
        when(grondslagRepository.findByPersoonId(any())).thenReturn(stored);
        Rule<?> failingRule = mock(Rule.class);
        when(failingRule.dependsOn()).thenReturn(DependencySet.of());
        when(failingRule.execute(any())).thenThrow(new FunctionalCalculationException(CalculationError.UNKNOWN_VEHICLE));
        calculationRules.put(EXAMPLE_JOURNEY, failingRule);

        Grondslag grondslag = treeCalculator.calculateResult(EXAMPLE_JOURNEY, testObject, LocalDate.of(2024, 1, 1));

        assertThat(grondslag.getGrondslaggegevens(), contains(existing));
        assertThat(existing.getWaarde(), is(nullValue()));
        assertThat(existing.getCalculationError(), is(CalculationError.UNKNOWN_VEHICLE));
    }

    private Definitie definitie(final Definitiecode definitiecode) {
        return new Definitie(null, definitiecode, Set.of());
    }

    private Grondslaggegeven<?> findByDefinitiecode(final Grondslag grondslag, final Definitiecode definitiecode) {
        return grondslag.getGrondslaggegevens().stream()
                .filter(gegeven -> gegeven.getDefinitie().getDefinitiecode() == definitiecode)
                .findFirst()
                .orElseThrow();
    }

    private Rule<?> stubRule(final Definitiecode definitiecode, final DependencySet dependencySet, final Function<CalculationContext, Object> resultFunction) {
        Rule<?> rule = mock(Rule.class);
        when(rule.dependsOn()).thenReturn(dependencySet);
        when(rule.execute(any())).thenAnswer(invocation -> {
            CalculationContext context = invocation.getArgument(0);
            Waarde<Object> result = new Waarde<>(resultFunction.apply(context), null);
            context.addCalculatedRule(definitiecode, result);
            return result;
        });
        return rule;
    }
}
