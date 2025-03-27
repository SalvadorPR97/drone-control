package com.salvador.droneControl.domain.model;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Schema(description = "Entidad de una matriz")
@Entity
@Data
@NoArgsConstructor
@Table(name = "matrices")
public class Matrix {

    @Schema(description = "Id de la matriz", example = "N")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Schema(description = "Valor máximo del eje X de la matriz", example = "5")
    @Column(nullable = false)
    private int max_x;

    @Schema(description = "Valor máximo del eje Y de la matriz", example = "5")
    @Column(nullable = false)
    private int max_y;

    @Schema(description = "Lista de drones de la matriz")
    @OneToMany(mappedBy = "matriz", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Drone> drones;
}
