package nl.svb.bre.engine.domain;

import nl.svb.bre.engine.domain.enums.ExampleVehicle;

public record ExampleObject(
    Integer distance,
    Boolean electric,
    ExampleVehicle vehicle
) { }
