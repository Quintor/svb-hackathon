package nl.svb.bre.engine.domain;

import nl.svb.bre.engine.domain.enums.ExampleVehicle;

public record ExampleObject (
    Long persoonId,
    Integer distance,
    Boolean electric,
    ExampleVehicle vehicle
) {

    public ExampleObject(Integer distance, Boolean electric, ExampleVehicle vehicle) {
        this(42L, distance, electric, vehicle);
    }
}
