package com.salvador.droneControl.application.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DroneSalidaDTO {

    private long id;
    @NotBlank
    private String nombre;
    @NotBlank
    private String modelo;
    @PositiveOrZero(message = "El valor del eje X debe ser positivo")
    private int x;
    @PositiveOrZero(message = "El valor del eje Y debe ser positivo")
    private int y;
    @Pattern(regexp = "[NSEO]", message = "Orientación inválida")
    private String orientacion;
}
