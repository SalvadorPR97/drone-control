package com.salvador.droneControl.application.dto;

import com.salvador.droneControl.domain.model.Movimientos;
import com.salvador.droneControl.infrastructure.validation.DroneExists;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO de los datos del dron para mover varios en una matriz")
@Data
@NoArgsConstructor
public class DroneEntradaDTO {

    @Schema(description = "Id del dron", example = "1")
    @DroneExists
    private long id;

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

    @Schema(description = "Lista de Movimientos que seguirá el dron",
            example = "[MOVE_FORWARD,TURN_LEFT,TURN_RIGHT]")
    private Movimientos[] orden;

}
