package com.salvador.droneControl.application.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DatosEntradaDTO {

    private MatrixEntradaDTO matriz;
    private DroneEntradaDTO[] drones;

}
