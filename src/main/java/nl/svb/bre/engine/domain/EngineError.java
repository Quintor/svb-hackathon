package nl.svb.bre.engine.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nl.svb.bre.engine.domain.enums.CalculationError;
import nl.svb.bre.engine.errors.CalculationException;

import java.util.Set;

@Getter
@EqualsAndHashCode(exclude = {"exception"})
@RequiredArgsConstructor
public class EngineError {

    private final boolean functional;
    private final CalculationException exception;
    private final Set<CalculationError> errors;

}
