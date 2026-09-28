package nl.svb.bre.engine.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CalculationError {

    UNKNOWN_VEHICLE("1000", "The requested vehicle is unsupported"),
    NO_ELECTRIC_BICYCLE("2000", "This is not an electric bicycle"),
    UNKNOWN_VALUE("8000", "Unknown value"),
    UNKNOWN_ERROR("9000", "Unknown error");

    private final String code;
    private final String message;
}
