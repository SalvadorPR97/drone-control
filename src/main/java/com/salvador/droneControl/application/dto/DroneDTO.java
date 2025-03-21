package com.salvador.droneControl.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DroneDTO {

    @JsonIgnore
    private long id;
    @NotBlank
    private String nombre;
    @NotBlank
    private String modelo;
    @PositiveOrZero(message = "El valor del eje X debe ser positivo")
    private Integer x;
    @PositiveOrZero(message = "El valor del eje Y debe ser positivo")
    private int y;
    @Pattern(regexp = "[NSEO]", message = "Orientación inválida")
    private String orientacion;
    // TODO Añadir validador que compruebe que la matriz existe
    @PositiveOrZero(message = "El valor debe ser positivo")
    private Long matriz_id;

    /*@JsonProperty
    public Long getId() {
        return id;
    }

    @JsonIgnore
    public void setId(Long id) {
        this.id = id;
    }*/
}
