package com.salvador.droneControl.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.salvador.droneControl.domain.model.Orientacion;
import com.salvador.droneControl.infrastructure.persistence.entity.MatrixEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DroneDTO {

    @JsonIgnore
    private long id;
    private String nombre;
    private String modelo;
    private int x;
    private int y;
    private Orientacion orientacion;
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
