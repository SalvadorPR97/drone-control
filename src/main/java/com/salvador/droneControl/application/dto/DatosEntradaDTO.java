package com.salvador.droneControl.application.dto;

import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DatosEntradaDTO {

    private MatrixEntradaDTO matriz;
    private DroneEntradaDTO[] drones;

}
