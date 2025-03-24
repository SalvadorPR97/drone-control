package com.salvador.droneControl.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MatrixEntradaDTO {

    @Positive(message = "El valor del eje X debe ser positivo")
    private int max_x;
    @Positive(message = "El valor del eje Y debe ser positivo")
    private int max_y;

}
