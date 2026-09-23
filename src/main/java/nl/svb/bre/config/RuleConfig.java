package nl.svb.bre.config;

import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.rules.Rule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Configuration
@ComponentScan
public class RuleConfig {

    @Bean
    public Map<Definitiecode, Rule<?>> calculationRules(List<Rule<?>> rules) {
        return rules.stream().collect(toMap(Rule::getDefinitionCode, rule -> rule));
    }
}
