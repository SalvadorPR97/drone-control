package com.salvador.droneControl.application.dto;

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
    @PositiveOrZero(message = "El valor debe ser positivo")
    private Long matriz_id;

}
