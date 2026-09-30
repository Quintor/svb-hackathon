package nl.svb.bre.engine.rules;

import nl.svb.bre.domain.Grondslag;
import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.TestObject;
import nl.svb.bre.engine.domain.Waarde;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BerekenLeeftijd2Test {
    @Test
    void executeRule() {
        LocalDate pijlDatum = LocalDate.of(2026, Month.SEPTEMBER,28);
        CalculationContext calculationContext = new CalculationContext(null, getTestObject(), pijlDatum, null);
        BerekenLeeftijd2 rule = new BerekenLeeftijd2();
        Waarde<Integer> execute = rule.execute(calculationContext);
        assertEquals(execute.value(), Integer.valueOf(2));
    }

    @Test
    void executeRuleOlder() {
        LocalDate pijlDatum = LocalDate.of(2026, Month.SEPTEMBER,28);
        CalculationContext calculationContext = new CalculationContext(null, getTestObject2(), pijlDatum, null);
        BerekenLeeftijd2 rule = new BerekenLeeftijd2();
        Waarde<Integer> execute = rule.execute(calculationContext);
        assertEquals(execute.value(), Integer.valueOf(3));
    }

    TestObject getTestObject() {
        return new TestObject(0L, List.of(1L, 2L), false,
                null, null, null, false, false, "",
                false, false, false,
                false, "", false,
                false, false, false,
                false, false, false, false,
                false, false, false,
                "", false, false,
                false, null, false, false,
                false, false, LocalDate.of(2014, 10, 10),
                LocalDate.of(2024, 4, 5), null, null);
    }

    TestObject getTestObject2() {
        return new TestObject(0L, List.of(1L, 2L), false,
                null, null, null, false, false, "",
                false, false, false,
                false, "", false,
                false, false, false,
                false, false, false, false,
                false, false, false,
                "", false, false,
                false, null, false, false,
                false, false, LocalDate.of(2014, 10, 10),
                LocalDate.of(2024, 12, 5), null, null);
    }
}
