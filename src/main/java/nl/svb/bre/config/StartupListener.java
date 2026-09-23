package nl.svb.bre.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nl.svb.bre.domain.Definitie;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.domain.Dependency;
import nl.svb.bre.engine.domain.DependencySet;
import nl.svb.bre.engine.rules.Rule;
import nl.svb.bre.repository.DefinitieRepository;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupListener implements ApplicationListener<ApplicationReadyEvent> {

    private final Map<Definitiecode, Rule<?>> calculationRules;
    private final DefinitieRepository definitieRepository;

    @Override
    @Transactional
    public void onApplicationEvent(ApplicationReadyEvent event) {
        log.info("BreApplication started with {} calculation rules loaded", calculationRules.size());

        calculationRules.keySet().forEach(this::updateDefinitie);
        log.debug("Persisted {}", definitieRepository.count());
    }


    private Definitie updateDefinitie(final Definitiecode definitiecode) {
        Rule<?> rule = calculationRules.get(definitiecode);
        Definitie existing = definitieRepository.findByDefinitiecode(definitiecode);
        if (existing != null && equalOnderliggend(existing.getOnderliggendeDefinities(), rule.dependsOn())) {
            return existing;
        }
        Set<Definitie> onderliggende = rule.dependsOn().stream()
                .map(dependency -> updateDefinitie(dependency.getDefinitiecode()))
                .collect(Collectors.toSet());
        return definitieRepository.save(new Definitie(null, definitiecode, onderliggende));
    }

    private boolean equalOnderliggend(final Set<Definitie> onderliggend, final DependencySet dependencies) {
        if (onderliggend.size() != dependencies.size()) {
            return false;
        }
        Set<Definitiecode> persisted = onderliggend.stream()
                .map(Definitie::getDefinitiecode)
                .collect(Collectors.toSet());
        Set<Definitiecode> current = dependencies.stream()
                .map(Dependency::getDefinitiecode)
                .collect(Collectors.toSet());
        return persisted.equals(current);
    }
}
