package com.salvador.droneControl.domain.model;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Drone {

    private long id;
    private String nombre;
    private String modelo;
    private int x;
    private int y;
    private Orientacion orientacion;
    private Long matriz_id;
}
