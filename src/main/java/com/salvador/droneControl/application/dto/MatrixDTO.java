package com.salvador.droneControl.application.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Orientación del dron")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MatrixDTO {

    @PositiveOrZero(message = "el id debe ser 0 o mayor")
    private Long id;
    @Positive(message = "El valor del eje X debe ser positivo")
    private int max_x;
    @Positive(message = "El valor del eje Y debe ser positivo")
    private int max_y;

}
