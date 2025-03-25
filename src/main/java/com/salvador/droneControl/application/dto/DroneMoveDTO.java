package com.salvador.droneControl.application.dto;

import com.salvador.droneControl.domain.model.Movimientos;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DroneMoveDTO {

    @PositiveOrZero
    private long id;
    @PositiveOrZero(message = "El valor debe ser positivo")
    private Long matrizId;
    private Movimientos[] orden;

}
