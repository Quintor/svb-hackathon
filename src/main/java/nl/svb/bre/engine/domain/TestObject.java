package nl.svb.bre.engine.domain;

import nl.svb.bre.engine.domain.enums.ExampleVehicle;

public record TestObject (
        Integer distance,
        Boolean electric,
        ExampleVehicle vehicle
) { }
