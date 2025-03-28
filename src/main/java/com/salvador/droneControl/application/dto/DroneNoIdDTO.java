package com.salvador.droneControl.application.dto;

import com.salvador.droneControl.infrastructure.validation.MatrixExists;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO del dron sin campo id")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DroneNoIdDTO {

    @Schema(description = "Nombre del dron", example = "Dron Alfa")
    @NotBlank(message = "el nombre no debe estar vacío")
    private String nombre;

    @Schema(description = "Modelo del dron", example = "X300")
    @NotBlank(message = "el modelo no debe estar vacío")
    private String modelo;

    @Schema(description = "Valor de la coordenada x del dron", example = "1")
    @PositiveOrZero(message = "El valor del eje X debe ser positivo")
    private int x;

    @Schema(description = "Valor de la coordenada y del dron", example = "1")
    @PositiveOrZero(message = "El valor del eje Y debe ser positivo")
    private int y;

    @Schema(description = "Orientación del dron", example = "N")
    @Pattern(regexp = "[NSEO]", message = "Orientación inválida")
    private String orientacion;

    @Schema(description = "Id de la matriz a la que está asociado el dron", example = "1")
    @MatrixExists
    private Long matrizId;
}
