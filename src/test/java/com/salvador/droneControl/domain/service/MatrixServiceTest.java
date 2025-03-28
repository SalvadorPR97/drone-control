package com.salvador.droneControl.domain.service;

import com.salvador.droneControl.application.dto.MatrixDTO;
import com.salvador.droneControl.application.dto.MatrixEntradaDTO;
import com.salvador.droneControl.application.mapper.MatrixMapper;
import com.salvador.droneControl.domain.model.Drone;
import com.salvador.droneControl.domain.model.Matrix;
import com.salvador.droneControl.domain.model.Orientacion;
import com.salvador.droneControl.domain.repository.MatrixRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
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

    @Spy
    @InjectMocks
    private MatrixService matrixService;

    @Test
    void getMatrixById() {
        Matrix matrix = new Matrix(1L, 5, 5, new ArrayList<>());

        when(matrixRepository.findById(1L)).thenReturn(Optional.of(matrix));

        Matrix result = matrixService.getMatrixById(1L);

        assertEquals(matrix, result);
        verify(matrixRepository).findById(1L);
    }

    @Test
    void saveMatrixEntity() {
        Matrix matrix = new Matrix(1L, 5, 5, new ArrayList<>());

        when(matrixRepository.save(matrix)).thenReturn(matrix);

        Matrix result = matrixService.saveMatrix(matrix);

        assertEquals(matrix, result);
        verify(matrixRepository).save(matrix);
    }


    @Test
    void createMatrix() {
        // Arrange
        MatrixEntradaDTO matrixEntradaDTO = new MatrixEntradaDTO(5, 5);
        MatrixDTO matrixDTO = new MatrixDTO();
        matrixDTO.setMax_x(matrixEntradaDTO.getMax_x());
        matrixDTO.setMax_y(matrixEntradaDTO.getMax_y());

        Matrix mappedMatrix = new Matrix(null, 5, 5, new ArrayList<>());
        Matrix savedMatrix = new Matrix(1L, 5, 5, new ArrayList<>());

        when(matrixMapper.mapMatrixDTOToMatrix(any(MatrixDTO.class))).thenReturn(mappedMatrix);

        when(matrixRepository.save(any(Matrix.class))).thenReturn(savedMatrix);

        MatrixDTO result = matrixService.createMatrix(matrixEntradaDTO);

        assertEquals(matrixEntradaDTO.getMax_x(), result.getMax_x());
        assertEquals(matrixEntradaDTO.getMax_y(), result.getMax_y());
        assertNotNull(result.getId());

        verify(matrixMapper, times(1)).mapMatrixDTOToMatrix(any(MatrixDTO.class));
        verify(matrixRepository, times(1)).save(any(Matrix.class));
    }

    @Test
    void updateMatrix() {
        Matrix oldMatrix = new Matrix(1L, 5, 5, new ArrayList<>());
        List<Drone> droneList = new ArrayList<>();
        Drone drone1 = new Drone(1L, "Drone1", "Test", 0, 0, Orientacion.N, oldMatrix);
        Drone drone2 = new Drone(2L, "Drone2", "Test", 0, 0, Orientacion.N, oldMatrix);
        droneList.add(drone1);
        droneList.add(drone2);
        oldMatrix.setDrones(droneList);

        MatrixEntradaDTO matrixEntradaDTO = new MatrixEntradaDTO(3, 3);

        when(matrixRepository.findById(1L)).thenReturn(Optional.of(oldMatrix));

        when(matrixRepository.save(any(Matrix.class))).thenReturn(oldMatrix);

        Matrix result = matrixService.updateMatrix(matrixEntradaDTO, 1L);

        assertEquals(matrixEntradaDTO.getMax_x(), result.getMax_x());
        assertEquals(matrixEntradaDTO.getMax_y(), result.getMax_y());
        assertNotNull(result.getDrones());

        verify(matrixRepository).findById(1L);
        verify(matrixRepository).save(oldMatrix);

        verify(matrixService, times(2)).droneOutOfMatrix(any(Drone.class), eq(oldMatrix));  // Espiamos la llamada a droneOutOfMatrix

    }

    @Test
    void deleteMatrixById() {
        Matrix matrix = new Matrix(1L, 5, 5, new ArrayList<>());
        MatrixDTO matrixDTO = new MatrixDTO(1L, 5, 5);

        when(matrixRepository.findById(1L)).thenReturn(Optional.of(matrix));

        doNothing().when(matrixRepository).deleteById(1L);

        when(matrixMapper.mapMatrixToMatrixDTO(matrix)).thenReturn(matrixDTO);

        MatrixDTO result = matrixService.deleteMatrixById(1L);

        assertEquals(matrixDTO.getId(), result.getId());
        assertEquals(matrixDTO.getMax_x(), result.getMax_x());
        assertEquals(matrixDTO.getMax_y(), result.getMax_y());

        verify(matrixRepository).findById(1L);  // Verifica que se haya llamado a findById
        verify(matrixRepository).deleteById(1L);  // Verifica que se haya llamado a deleteById
        verify(matrixMapper).mapMatrixToMatrixDTO(matrix);  // Verifica que se haya mapeado correctamente la matriz a DTO
    }
}