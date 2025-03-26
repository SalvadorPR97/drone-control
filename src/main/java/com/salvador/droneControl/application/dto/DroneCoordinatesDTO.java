package com.salvador.droneControl.application.dto;

import com.salvador.droneControl.infrastructure.validation.MatrixExists;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DroneCoordinatesDTO {

    @PositiveOrZero(message = "El valor del eje X debe ser positivo")
    private int x;
    @PositiveOrZero(message = "El valor del eje Y debe ser positivo")
    private int y;
    @MatrixExists
    private Long matriz_id;

}
