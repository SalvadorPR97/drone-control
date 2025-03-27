package com.salvador.droneControl.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO para crear una matriz")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatrixEntradaDTO {

    @Schema(description = "Valor máximo del eje X de la matriz", example = "5")
    @Positive(message = "El valor del eje X debe ser positivo")
    private int max_x;

    @Schema(description = "Valor máximo del eje Y de la matriz", example = "5")
    @Positive(message = "El valor del eje Y debe ser positivo")
    private int max_y;

}
