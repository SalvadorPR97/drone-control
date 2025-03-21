package com.salvador.droneControl.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class Matrix {

    private long id;
    private int max_x;
    private int max_y;
    private List<Long> drones_ids;
}
