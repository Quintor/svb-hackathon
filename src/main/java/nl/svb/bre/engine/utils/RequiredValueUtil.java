package nl.svb.bre.engine.utils;

import nl.svb.bre.engine.domain.enums.CalculationError;
import nl.svb.bre.engine.errors.FunctionalCalculationException;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public final class RequiredValueUtil {
    private RequiredValueUtil() {
    }

    public static <T> @NonNull T requiredValue(@Nullable T value) {
        if (value == null) {
            throw new FunctionalCalculationException(CalculationError.UNKNOWN_VALUE);
        }
        return value;
    }
}
