package com.salvador.droneControl.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.Map;

@Schema(description = "Direcciones posibles para la orientación del dron")
public enum Orientacion {

    @Schema(description = "Norte") N,
    @Schema(description = "Sur") S,
    @Schema(description = "Este") E,
    @Schema(description = "Oeste") O;

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

    /**
     * Gira la orientación a la izquierda.
     *
     * @return Nueva orientación después de girar a la izquierda.
     */
    public Orientacion turnLeft() {
        return LEFT_MAP.get(this);
    }

    /**
     * Gira la orientación a la derecha.
     *
     * @return Nueva orientación después de girar a la derecha.
     */
    public Orientacion turnRight() {
        return RIGHT_MAP.get(this);
    }
}
