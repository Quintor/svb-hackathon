package nl.svb.bre.engine.rules;

import nl.svb.bre.engine.context.CalculationContext;
import nl.svb.bre.engine.domain.ExampleObject;
import nl.svb.bre.engine.domain.TestObject;
import nl.svb.bre.engine.domain.Waarde;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class BerekenLeeftijd2Test {
    @Test
    void executeRule() {
        CalculationContext calculationContext = new CalculationContext(null, getTestObject(), null);
        BerekenLeeftijd2 rule = new BerekenLeeftijd2();
        Waarde<Integer> execute = rule.execute(calculationContext);
        assertEquals(execute.value(), Integer.valueOf(2));
    }

    @Test
    void executeRuleOlder() {
        CalculationContext calculationContext = new CalculationContext(null, getTestObject2(), null);
        BerekenLeeftijd2 rule = new BerekenLeeftijd2();
        Waarde<Integer> execute = rule.execute(calculationContext);
        assertEquals(execute.value(), Integer.valueOf(3));
    }

    TestObject getTestObject() {
        return new TestObject(0l, List.of(1l, 2l), false,
                null, null, null, false, false, "",
                false, false, false,
                false, "", false,
                false, false, false,
                false, false, false, false,
                false, false, false,
                "", false, false,
                false, null, false, false,
                false, false, LocalDate.of(2014, 10, 10),
                LocalDate.of(2024, 4, 5));
    }

    TestObject getTestObject2() {
        return new TestObject(0l, List.of(1l, 2l), false,
                null, null, null, false, false, "",
                false, false, false,
                false, "", false,
                false, false, false,
                false, false, false, false,
                false, false, false,
                "", false, false,
                false, null, false, false,
                false, false, LocalDate.of(2014, 10, 10),
                LocalDate.of(2024, 12, 5));
    }
}