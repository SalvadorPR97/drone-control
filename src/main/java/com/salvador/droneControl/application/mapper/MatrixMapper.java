package com.salvador.droneControl.application.mapper;

import com.salvador.droneControl.application.dto.MatrixDataDTO;
import com.salvador.droneControl.domain.model.Matrix;
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

    public Matrix mapMatrixDTOToMatrixEntity(MatrixDataDTO matrixDataDTO) {
        return modelMapper.map(matrixDataDTO, Matrix.class);
    }

    public MatrixDataDTO mapMatrixEntityToMatrixDTO(Matrix matrix) {
        return modelMapper.map(matrix, MatrixDataDTO.class);
    }

}
