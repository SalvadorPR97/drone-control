package com.salvador.droneControl.domain.model;


import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
public class Drone {

    private long id;
    private String nombre;
    private String modelo;
    private int x;
    private int y;
    private Orientacion orientacion;
}
