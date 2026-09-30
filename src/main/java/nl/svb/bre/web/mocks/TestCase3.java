package nl.svb.bre.web.mocks;

import lombok.experimental.UtilityClass;
import nl.svb.bre.engine.domain.TestObject;

import java.time.LocalDate;
import java.util.List;

@UtilityClass
public class TestCase3 {

        public static TestObject TEST_OBJECT(final Long persoonId, final Long persoonIdKind1, final Long persoonIdKind2) {
            return new TestObject(
                    persoonId,
                    List.of(persoonIdKind1, persoonIdKind2),
                    true,
                    null,
                    null,
                    null,
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
                    "Unknown string for uitworp",
                    true,
                    false,
                    false,
                    null,
                    false,
                    false,
                    false,
                    false,
                    LocalDate.of(2016, 3, 25),
                    LocalDate.of(2018, 4, 1),
                    null,null
            );
    }

}
