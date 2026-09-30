package nl.svb.bre.engine.context;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.svb.bre.domain.Grondslag;
import nl.svb.bre.domain.enums.Definitiecode;
import nl.svb.bre.engine.domain.ExampleObject;
import nl.svb.bre.engine.domain.TestObject;
import nl.svb.bre.engine.domain.Waarde;

import java.time.LocalDate;
import java.util.EnumMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class CalculationContext {

    private final ExampleObject exampleObject;
    private final TestObject testObject;
    private final LocalDate peildatum;
    private final Grondslag grondslag;
    private final Map<Definitiecode, Waarde<?>> calculated = new EnumMap<>(Definitiecode.class);

    public void addCalculatedRule(final Definitiecode definitiecode, Waarde<?> value) {
        calculated.put(definitiecode, value);
    }

    public Object getCalculated(final Definitiecode definitiecode) {
        return calculated.get(definitiecode).value();
    }

    public boolean isCalculated(final Definitiecode definitiecode) {
        return calculated.containsKey(definitiecode);
    }
}
