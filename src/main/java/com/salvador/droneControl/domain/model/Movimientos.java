package com.salvador.droneControl.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Movimientos posibles para un dron")
public enum Movimientos {
    @Schema(description = "Rota a la izquierda 90º") TURN_LEFT,
    @Schema(description = "Rota a la derecha 90º") TURN_RIGHT,
    @Schema(description = "Avanza hacia adelante") MOVE_FORWARD
}
