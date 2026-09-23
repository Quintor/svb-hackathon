package nl.svb.bre.config;

import nl.svb.bre.domain.Definitie;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.rules.Rule;
import nl.svb.bre.repository.DefinitieRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static nl.svb.bre.domain.enums.Definitiecode.EXAMPLE_JOURNEY;
import static nl.svb.bre.domain.enums.Definitiecode.EXAMPLE_VEHICLE;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StartupListenerTest {

    @Mock
    private DefinitieRepository definitieRepository;

    private final Map<Definitiecode, Rule<?>> calculationRules = new HashMap<>();
    private StartupListener startupListener;

    @BeforeEach
    void setUp() {
        startupListener = new StartupListener(calculationRules, definitieRepository);
    }

    private static Stream<Arguments> newOrUnchangedDefinitieScenarios() {
        return Stream.of(
                Arguments.of("no Definitie persisted yet", null, true),
                Arguments.of("Definitie already matches current dependencies", new Definitie(5L, EXAMPLE_VEHICLE, Set.of()), false)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("newOrUnchangedDefinitieScenarios")
    void onApplicationEvent_definitieWithoutDependencies_persistsOnlyWhenMissing(final String scenario, final Definitie existing, final boolean expectSave) {
        calculationRules.put(EXAMPLE_VEHICLE, stubRule());
        when(definitieRepository.findByDefinitiecode(EXAMPLE_VEHICLE)).thenReturn(existing);
        if (expectSave) {
            when(definitieRepository.save(any(Definitie.class))).thenAnswer(invocation -> invocation.getArgument(0));
        }

        startupListener.onApplicationEvent(mock(ApplicationReadyEvent.class));

        verify(definitieRepository, times(expectSave ? 1 : 0)).save(any(Definitie.class));
    }

    private static Stream<Arguments> dependencyUpdateScenarios() {
        return Stream.of(
                Arguments.of("neither vehicle nor journey persisted yet", null, null, 2),
                Arguments.of("vehicle unchanged, journey missing its dependency", new Definitie(1L, EXAMPLE_VEHICLE, Set.of()), new Definitie(2L, EXAMPLE_JOURNEY, Set.of()), 1)
        );
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("dependencyUpdateScenarios")
    void onApplicationEvent_journeyDependsOnVehicle_savesOnlyWhatChanged(final String scenario, final Definitie existingVehicle, final Definitie existingJourney, final int expectedSaveCount) {
        calculationRules.put(EXAMPLE_VEHICLE, stubRule());
        calculationRules.put(EXAMPLE_JOURNEY, stubRule(Dependency.of(EXAMPLE_VEHICLE)));
        stubStatefulRepository(existingVehicle, existingJourney);

        startupListener.onApplicationEvent(mock(ApplicationReadyEvent.class));

        verify(definitieRepository, times(expectedSaveCount)).save(any(Definitie.class));
    }

    @Test
    void onApplicationEvent_existingDefinitieWithDifferentDependencies_isRebuiltWithCurrentOnderliggend() {
        calculationRules.put(EXAMPLE_VEHICLE, stubRule());
        calculationRules.put(EXAMPLE_JOURNEY, stubRule(Dependency.of(EXAMPLE_VEHICLE)));

        Definitie existingVehicle = new Definitie(1L, EXAMPLE_VEHICLE, Set.of());
        Definitie existingJourney = new Definitie(2L, EXAMPLE_JOURNEY, Set.of());
        stubStatefulRepository(existingVehicle, existingJourney);

        startupListener.onApplicationEvent(mock(ApplicationReadyEvent.class));

        ArgumentCaptor<Definitie> captor = ArgumentCaptor.forClass(Definitie.class);
        verify(definitieRepository, times(1)).save(captor.capture());
        assertThat(captor.getValue().getDefinitiecode(), is(EXAMPLE_JOURNEY));
        assertThat(captor.getValue().getOnderliggendeDefinities(), contains(existingVehicle));
    }

    private void stubStatefulRepository(final Definitie existingVehicle, final Definitie existingJourney) {
        Map<Definitiecode, Definitie> persisted = new HashMap<>();
        if (existingVehicle != null) {
            persisted.put(EXAMPLE_VEHICLE, existingVehicle);
        }
        if (existingJourney != null) {
            persisted.put(EXAMPLE_JOURNEY, existingJourney);
        }
        when(definitieRepository.findByDefinitiecode(any())).thenAnswer(invocation -> persisted.get(invocation.getArgument(0)));
        when(definitieRepository.save(any(Definitie.class))).thenAnswer(invocation -> {
            Definitie definitie = invocation.getArgument(0);
            persisted.put(definitie.getDefinitiecode(), definitie);
            return definitie;
        });
    }

    private Rule<?> stubRule(final Dependency... dependencies) {
        Rule<?> rule = mock(Rule.class);
        when(rule.dependsOn()).thenReturn(DependencySet.of(dependencies));
        return rule;
    }
}
