package com.salvador.droneControl.infrastructure.persistence.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.salvador.droneControl.domain.model.Orientacion;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "drones")
public class DroneEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String modelo;

    @Column(nullable = false)
    private int x;

    @Column(nullable = false)
    private int y;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Orientacion orientacion;

    @ManyToOne
    @JoinColumn(name = "matriz_id")
    @JsonBackReference
    private MatrixEntity matriz;
}
