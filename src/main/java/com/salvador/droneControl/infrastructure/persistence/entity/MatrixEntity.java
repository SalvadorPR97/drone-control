package com.salvador.droneControl.infrastructure.persistence.entity;


import com.salvador.droneControl.domain.model.Drone;
import com.salvador.droneControl.domain.model.Orientacion;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "matrices")
public class MatrixEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private int max_x;

    @Column(nullable = false)
    private int max_y;

    @OneToMany(mappedBy = "matriz")
    private List<DroneEntity> drones;
}
