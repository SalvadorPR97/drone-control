package com.salvador.droneControl.application.dto;

import com.salvador.droneControl.domain.model.Movimientos;
import com.salvador.droneControl.infrastructure.validation.MatrixExists;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DroneMoveDTO {

    @PositiveOrZero(message = "el id debe ser 0 o mayor")
    private long id;
    @MatrixExists
    private Long matrizId;
    private Movimientos[] orden;

}
