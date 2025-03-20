package com.salvador.droneControl.application.mapper;

import com.salvador.droneControl.domain.model.Drone;
import com.salvador.droneControl.domain.model.Matrix;
import com.salvador.droneControl.infrastructure.persistence.entity.DroneEntity;
import com.salvador.droneControl.infrastructure.persistence.entity.MatrixEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MatrixMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public MatrixMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Matrix mapToMatrix(MatrixEntity matrixEntity) {
        return modelMapper.map(matrixEntity, Matrix.class);
    }

    public MatrixEntity mapToMatrixEntity(Matrix matrix) {
        return modelMapper.map(matrix, MatrixEntity.class);
    }
}
