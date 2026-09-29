package nl.svb.bre.engine.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CalculationError {

    UNKNOWN_VEHICLE("1000", "The requested vehicle is unsupported"),
    NO_ELECTRIC_BICYCLE("2000", "This is not an electric bicycle"),
    MISSING_VALUE_D12111("D12111", "conditie D113111 is null"),
    MISSING_VALUE_D12112("D12112", "Benodigde waarde ontbreekt: 'tijdelijk geestelijk onbekwaam'" ),
    MISSING_VALUE_D12113("D12113", "Er ontbreekt een waarde voor 'de persoon heeft inlichtingen verstrekt die onvolledig waren maar uit eigen beweging alsnog de juiste inlichtingen verstrekt voordat de overtreding is geconstateerd'"),
    MISSING_VALUE_D12131_A("D12131", "Benodidigde Waarde ontbreekt: gedurende de afgelopen twee jaar voor de huidige overtreding is er minimaal één maatregel bekendgemaakt vanwege het niet nakomen van een verplichting van de tweede categorie o.g.v. dezelfde wet" ),
    MISSING_VALUE_D12131_B("D12131", "Benodidigde Waarde ontbreekt: gedurende de afgelopen twee jaar voor de huidige overtreding is er minimaal één maatregel bekendgemaakt vanwege het niet nakomen van de overige controlevoorschriften o.g.v. dezelfde wet"),
    UNKNOWN_VALUE("8000", "Unknown value"),
    UNKNOWN_ERROR("9000", "Unknown error");

    private final String code;
    private final String message;
}
