package com.salvador.droneControl.application.mapper;

import com.salvador.droneControl.application.dto.MatrixDTO;
import com.salvador.droneControl.domain.model.Matrix;
import com.salvador.droneControl.infrastructure.persistence.entity.DroneEntity;
import com.salvador.droneControl.infrastructure.persistence.entity.MatrixEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class MatrixMapper {

    private final ModelMapper modelMapper;
    private final DroneMapper droneMapper;

    @Autowired
    public MatrixMapper(ModelMapper modelMapper, DroneMapper droneMapper) {
        this.modelMapper = modelMapper;
        this.droneMapper = droneMapper;
    }

    public Matrix mapToMatrix(MatrixEntity matrixEntity) {
        return modelMapper.map(matrixEntity, Matrix.class);
    }

    public MatrixEntity mapToMatrixEntity(Matrix matrix) {
        return modelMapper.map(matrix, MatrixEntity.class);
    }

}
