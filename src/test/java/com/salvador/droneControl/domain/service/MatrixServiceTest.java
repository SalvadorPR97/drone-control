package com.salvador.droneControl.domain.service;

import com.salvador.droneControl.application.dto.MatrixDTO;
import com.salvador.droneControl.application.dto.MatrixEntradaDTO;
import com.salvador.droneControl.application.mapper.MatrixMapper;
import com.salvador.droneControl.domain.model.Drone;
import com.salvador.droneControl.domain.model.Matrix;
import com.salvador.droneControl.domain.repository.MatrixRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MatrixServiceTest {

    @Mock
    private MatrixRepository matrixRepository;

    @Mock
    private MatrixMapper matrixMapper;

    @InjectMocks
    private MatrixService matrixService;

    /*@BeforeEach
    void setUp() {
    }*/

    @Test
    void getMatrixById() {
        Drone dron1 = new Drone();
        Drone dron2 = new Drone();
        List<Drone> droneList = new ArrayList<>();
        droneList.add(dron1);
        droneList.add(dron2);
        Matrix matrix = new Matrix(1, 5, 5, droneList);

        when(matrixRepository.findById(1L)).thenReturn(Optional.of(matrix));

        Matrix result = matrixService.getMatrixById(1L);

        assertEquals(matrix, result);
        verify(matrixRepository).findById(1L);
    }

    @Test
    void saveMatrixEntity() {
        Drone dron1 = new Drone();
        Drone dron2 = new Drone();
        List<Drone> droneList = new ArrayList<>();
        droneList.add(dron1);
        droneList.add(dron2);
        Matrix matrix = new Matrix(1, 5, 5, droneList);

        when(matrixRepository.save(matrix)).thenReturn(matrix);

        Matrix result = matrixService.saveMatrix(matrix);

        assertEquals(matrix, result);
        verify(matrixRepository).save(matrix);
    }


    @Test
    void createMatrix() {
        MatrixEntradaDTO matrixEntradaDTO = new MatrixEntradaDTO(5,5);
        MatrixDTO matrixDTO = new MatrixDTO(1L, 5,5);
        Drone dron1 = new Drone();
        Drone dron2 = new Drone();
        List<Drone> droneList = new ArrayList<>();
        droneList.add(dron1);
        droneList.add(dron2);
        Matrix matrix = new Matrix(1, 5, 5, droneList);
        when(matrixService.saveMatrix(matrixMapper.mapMatrixDTOToMatrix(matrixDTO))).thenReturn(matrix);

        MatrixDTO result = matrixService.createMatrix(matrixEntradaDTO);

        assertEquals(matrixEntradaDTO.getMax_x(), result.getMax_x());
        assertEquals(matrixEntradaDTO.getMax_y(), result.getMax_y());
        assertNotNull(result.getId());

        verify(matrixMapper, times(2)).mapMatrixDTOToMatrix(any(MatrixDTO.class));
    }
/*
    @Test
    void updateMatrix() {
    }

    @Test
    void deleteMatrixById() {
    }*/
}