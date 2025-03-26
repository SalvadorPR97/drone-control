package com.salvador.droneControl.domain.model;

import java.util.Map;

public enum Orientacion {
    N, S, E, O;

    private static final Map<Orientacion, Orientacion> LEFT_MAP = Map.of(
            N, O,
            S, E,
            E, N,
            O, S
    );

    private static final Map<Orientacion, Orientacion> RIGHT_MAP = Map.of(
            N, E,
            S, O,
            E, S,
            O, N
    );

    public Orientacion turnLeft() {
        return LEFT_MAP.get(this);
    }

    public Orientacion turnRight() {
        return RIGHT_MAP.get(this);
    }
}
