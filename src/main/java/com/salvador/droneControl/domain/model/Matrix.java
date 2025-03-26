package com.salvador.droneControl.domain.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "matrices")
public class Matrix {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private int max_x;

    @Column(nullable = false)
    private int max_y;

    @OneToMany(mappedBy = "matriz", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Drone> drones;
}
