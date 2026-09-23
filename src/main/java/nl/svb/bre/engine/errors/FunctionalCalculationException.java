package nl.svb.bre.engine.errors;

import lombok.Getter;
import lombok.ToString;
import nl.svb.bre.engine.domain.enums.CalculationError;

@Getter
@ToString
public class FunctionalCalculationException extends CalculationException {

    private final CalculationError calculationError;

    public FunctionalCalculationException(final CalculationError calculationError) {
        this(calculationError, null, null);
    }

    public FunctionalCalculationException(final CalculationError calculationError, final Throwable cause) {
        this(calculationError, null, cause);
    }

    public FunctionalCalculationException(final CalculationError calculationError, final String message) {
        this(calculationError, message, null);
    }

    public FunctionalCalculationException(final CalculationError calculationError, final String message, final Throwable cause) {
        super(message, cause);
        this.calculationError = calculationError;
    }

    @Override
    public String getMessage() {
        return String.format("%s, code: %s, message: %s", calculationError.name(), calculationError.getCode(), super.getMessage());
    }
}
