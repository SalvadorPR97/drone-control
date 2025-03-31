package com.salvador.droneControl.application.mapper;

import com.salvador.droneControl.application.dto.MatrixDTO;
import com.salvador.droneControl.domain.model.Matrix;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class MatrixMapper {

    private final ModelMapper modelMapper;

    public MatrixMapper() {
        this.modelMapper = new ModelMapper();
    }

    public Matrix mapMatrixDTOToMatrix(MatrixDTO matrixDTO) {
        return modelMapper.map(matrixDTO, Matrix.class);
    }

    public MatrixDTO mapMatrixToMatrixDTO(Matrix matrix) {
        return modelMapper.map(matrix, MatrixDTO.class);
    }

}
