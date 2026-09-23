package nl.svb.bre.engine.domain;

import java.util.Arrays;
import java.util.LinkedHashSet;

public final class DependencySet extends LinkedHashSet<Dependency> {

    private DependencySet() {
    }

    public static DependencySet of(Dependency... d) {
        DependencySet result = new DependencySet();
        result.addAll(Arrays.asList(d));
        return result;
    }
}

