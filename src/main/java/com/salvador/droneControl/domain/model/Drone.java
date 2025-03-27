package com.salvador.droneControl.domain.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Schema(description = "Entidad de un dron")
@Entity
@Data
@NoArgsConstructor
@Table(name = "drones")
public class Drone {
    @Schema(description = "Id del dron", example = "1")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Nombre del dron", example = "Dron Alfa")
    @Column(nullable = false)
    private String nombre;

    @Schema(description = "Modelo del dron", example = "X300")
    @Column(nullable = false)
    private String modelo;

    @Schema(description = "Valor de la coordenada x del dron", example = "1")
    @Column(nullable = false)
    private int x;

    @Schema(description = "Valor de la coordenada y del dron", example = "1")
    @Column(nullable = false)
    private int y;

    @Schema(description = "Orientación del dron", example = "N")
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Orientacion orientacion;

    @Schema(description = "Matriz a la que está asociado el dron")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "matriz_id")
    @JsonBackReference
    private Matrix matriz;
}
