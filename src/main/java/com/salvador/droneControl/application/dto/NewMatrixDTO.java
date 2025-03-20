package com.salvador.droneControl.application.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class NewMatrixDTO {

    private Long id;
    @Positive(message = "El valor del eje X debe ser positivo")
    private int max_x;
    @Positive(message = "El valor del eje Y debe ser positivo")
    private int max_y;

    @JsonProperty
    public Long getId() {
        return id;
    }
    @JsonIgnore
    public void setId(Long id) {
        this.id = id;
    }

}
