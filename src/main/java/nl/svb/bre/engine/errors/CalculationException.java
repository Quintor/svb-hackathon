package nl.svb.bre.engine.errors;

public class CalculationException extends RuntimeException {

    protected CalculationException(final String message) {
        super(message);
    }

    protected CalculationException(final String message, final Throwable cause) {
        super(message, cause);
    }

    protected CalculationException(final Throwable cause) {
        super(cause);
    }
}
