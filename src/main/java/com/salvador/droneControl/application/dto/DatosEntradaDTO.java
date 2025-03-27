package com.salvador.droneControl.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO para los datos de entrada completo, matriz y drones que queremos mover")
@Data
@NoArgsConstructor
public class DatosEntradaDTO {
    @Schema(description = "El valor máximo de la x y la y de la matriz")
    private MatrixEntradaDTO matriz;
    @Schema(description = "Lista de drones con sus órdenes")
    private DroneEntradaDTO[] drones;

}
