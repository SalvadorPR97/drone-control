package com.salvador.droneControl.application.dto;


import com.salvador.droneControl.domain.model.Orientacion;
import com.salvador.droneControl.infrastructure.validation.MatrixExists;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO que representa un dron")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DroneDTO {

    @Schema(description = "Id del dron", example = "1")
    @PositiveOrZero(message = "El id debe ser 0 o mayor")
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
    private Orientacion orientacion;
    @MatrixExists

    @Schema(description = "Id de la matriz a la que está asociado el dron", example = "1")
    private Long matrizId;
}
