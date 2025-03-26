package com.salvador.droneControl.application.dto;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MatrixDataDTO {

    @PositiveOrZero(message = "el id debe ser 0 o mayor")
    private Long id;
    @Positive(message = "El valor del eje X debe ser positivo")
    private int max_x;
    @Positive(message = "El valor del eje Y debe ser positivo")
    private int max_y;

}
