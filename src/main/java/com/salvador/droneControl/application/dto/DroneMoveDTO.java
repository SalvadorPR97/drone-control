package com.salvador.droneControl.application.dto;

import com.salvador.droneControl.domain.model.Movimientos;
import com.salvador.droneControl.infrastructure.validation.MatrixExists;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO para mover un dron")
@Data
@NoArgsConstructor
public class DroneMoveDTO {

    @Schema(description = "Id del dron", example = "1")
    @PositiveOrZero(message = "el id debe ser 0 o mayor")
    private long id;

    @Schema(description = "Id de la matriz a la que está asociado el dron", example = "1")
    @MatrixExists
    private Long matrizId;

    @Schema(description = "Lista de Movimientos que seguirá el dron",
            example = "[MOVE_FORWARD,TURN_LEFT,TURN_RIGHT]")
    private Movimientos[] orden;

}
