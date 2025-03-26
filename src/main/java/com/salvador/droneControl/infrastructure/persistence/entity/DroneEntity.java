package com.salvador.droneControl.infrastructure.persistence.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.salvador.droneControl.domain.model.Orientacion;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Data
@NoArgsConstructor
@Table(name = "drones")
public class DroneEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

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
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "matriz_id")
    @JsonBackReference
    private MatrixEntity matriz;
}
