package com.salvador.droneControl.application.dto;

import com.salvador.droneControl.domain.model.Movimientos;
import com.salvador.droneControl.infrastructure.validation.DroneExists;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DroneEntradaDTO {

    @DroneExists
    private long id;
    @NotBlank(message = "el nombre no debe estar vacío")
    private String nombre;
    @NotBlank(message = "el modelo no debe estar vacío")
    private String modelo;
    @PositiveOrZero(message = "El valor del eje X debe ser positivo")
    private int x;
    @PositiveOrZero(message = "El valor del eje Y debe ser positivo")
    private int y;
    @Pattern(regexp = "[NSEO]", message = "Orientación inválida")
    private String orientacion;
    private Movimientos[] orden;

}
