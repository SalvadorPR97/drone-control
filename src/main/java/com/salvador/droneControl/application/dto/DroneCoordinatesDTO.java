package com.salvador.droneControl.application.dto;

import com.salvador.droneControl.infrastructure.validation.MatrixExists;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO de dron para buscar por sus coordenadas en una matriz")
@Data
@NoArgsConstructor
public class DroneCoordinatesDTO {

    @Schema(description = "Valor de x del dron", example = "1")
    @PositiveOrZero(message = "El valor del eje X debe ser positivo")
    private int x;
    @Schema(description = "Valor de y del dron", example = "1")
    @PositiveOrZero(message = "El valor del eje Y debe ser positivo")
    private int y;
    @Schema(description = "Id de la matriz del dron", example = "1")
    @MatrixExists
    private Long matriz_id;

}
