package nl.svb.bre.engine.domain;

import nl.svb.bre.domain.Grondslag;

import java.util.List;

public record EngineResult(
        Grondslag grondslag,
        List<EngineError> errors
) { }
