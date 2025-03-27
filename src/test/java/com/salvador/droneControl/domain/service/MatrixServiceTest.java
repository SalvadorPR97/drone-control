package com.salvador.droneControl.domain.service;

import com.salvador.droneControl.domain.model.Drone;
import com.salvador.droneControl.domain.model.Matrix;
import com.salvador.droneControl.domain.repository.MatrixRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MatrixServiceTest {

    @Mock
    private MatrixRepository matrixRepository;

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

    /*@Test
    void saveMatrixEntity() {
    }

    @Test
    void createMatrix() {
    }

    @Test
    void updateMatrix() {
    }

    @Test
    void deleteMatrixById() {
    }*/
}