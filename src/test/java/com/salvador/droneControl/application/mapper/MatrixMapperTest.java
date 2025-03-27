package com.salvador.droneControl.application.mapper;

import com.salvador.droneControl.application.dto.MatrixDTO;
import com.salvador.droneControl.domain.model.Matrix;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MatrixMapperTest {

    private MatrixMapper matrixMapper;

    @BeforeEach
    void setUp() {
        matrixMapper = new MatrixMapper();
    }

    @Test
    void mapMatrixDTOToMatrix() {
        MatrixDTO matrixDTO = new MatrixDTO(1L, 5, 5);

        Matrix mappedMatrixDTO = matrixMapper.mapMatrixDTOToMatrix(matrixDTO);
        assertEquals(matrixDTO.getId(), mappedMatrixDTO.getId());
        assertEquals(matrixDTO.getMax_x(), mappedMatrixDTO.getMax_x());
        assertEquals(matrixDTO.getMax_y(), mappedMatrixDTO.getMax_y());
    }

    @Test
    void mapMatrixToMatrixDTO() {
        Matrix matrix = new Matrix(1L, 5, 5, null);

        MatrixDTO mappedMatrixDTO = matrixMapper.mapMatrixToMatrixDTO(matrix);
        assertEquals(matrix.getId(), mappedMatrixDTO.getId());
        assertEquals(matrix.getMax_x(), mappedMatrixDTO.getMax_x());
        assertEquals(matrix.getMax_y(), mappedMatrixDTO.getMax_y());
    }
}