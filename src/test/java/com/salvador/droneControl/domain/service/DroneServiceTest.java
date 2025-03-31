package com.salvador.droneControl.domain.service;

import com.salvador.droneControl.application.dto.*;
import com.salvador.droneControl.application.mapper.DroneMapper;
import com.salvador.droneControl.domain.model.Drone;
import com.salvador.droneControl.domain.model.Matrix;
import com.salvador.droneControl.domain.model.Movimientos;
import com.salvador.droneControl.domain.model.Orientacion;
import com.salvador.droneControl.domain.repository.DroneRepository;
import com.salvador.droneControl.infrastructure.exception.ResourceNotFoundException;
import com.salvador.droneControl.infrastructure.exception.WrongCoordinatesException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DroneServiceTest {

    @Mock
    private DroneRepository droneRepository;

    @Mock
    private DroneMapper droneMapper;

    @Mock
    private MatrixService matrixService;

    @Spy
    @InjectMocks
    private DroneService droneService;

    private Drone drone;
    private Matrix matrix;
    private DroneNoIdDTO droneNoIdDTO;
    private DroneDTO droneDTO;

    @BeforeEach
    void setUp() {
        matrix = new Matrix(1L, 5, 5, new ArrayList<>());
        drone = new Drone(1L, "Test", "Test", 2, 2, Orientacion.N, matrix);
        droneNoIdDTO = new DroneNoIdDTO("Test", "Test", 2, 2, "N", 1L);
        droneDTO = new DroneDTO(1L, "Test", "Test", 2, 2, Orientacion.N, 1L);

    }

    @Test
    void getDroneById() {
        when(droneRepository.findById(1L)).thenReturn(Optional.of(drone));
        Drone result = droneService.getDroneById(1L);
        assertEquals(drone, result);
    }

    @Test
    void saveDrone() {
        when(droneRepository.save(drone)).thenReturn(drone);
        Drone result = droneService.saveDrone(drone);
        assertEquals(drone, result);
    }

    @Test
    void deleteDroneById() {
        when(droneRepository.findById(1L)).thenReturn(Optional.of(drone));
        droneService.deleteDroneById(1L);
        verify(droneRepository).delete(drone);
    }

    @Test
    void createDrone() {
        when(matrixService.getMatrixById(droneNoIdDTO.getMatrizId())).thenReturn(matrix);
        when(droneMapper.mapDroneNoIdDTOToDrone(droneNoIdDTO)).thenReturn(drone);
        when(droneRepository.save(drone)).thenReturn(drone);

        when(droneMapper.mapDroneToDroneDTO(drone)).thenReturn(droneDTO);
        DroneDTO result = droneService.createDrone(droneNoIdDTO);

        assertEquals(droneNoIdDTO.getNombre(), result.getNombre());
        assertEquals(droneNoIdDTO.getModelo(), result.getModelo());
        assertEquals(droneNoIdDTO.getX(), result.getX());
        assertEquals(droneNoIdDTO.getY(), result.getY());
        assertEquals(droneNoIdDTO.getMatrizId(), result.getMatrizId());

        verify(droneService).coordinatesBusy(drone, matrix);
        verify(droneService).coordinatesOutOfMatrix(drone, matrix);
    }

    @Test
    void updateDrone() {
        when(matrixService.getMatrixById(droneNoIdDTO.getMatrizId())).thenReturn(matrix);
        when(droneMapper.mapDroneNoIdDTOToDrone(droneNoIdDTO)).thenReturn(drone);

        when(droneRepository.save(drone)).thenReturn(drone);
        when(droneMapper.mapDroneToDroneDTO(drone)).thenReturn(droneDTO);

        DroneDTO result = droneService.updateDrone(droneNoIdDTO, 1L);

        assertEquals(drone.getId(), result.getId());
        assertEquals(drone.getNombre(), result.getNombre());
        assertEquals(drone.getModelo(), result.getModelo());
        assertEquals(drone.getX(), result.getX());
        assertEquals(drone.getY(), result.getY());
        assertEquals(drone.getMatriz().getId(), result.getMatrizId());

        verify(droneService).coordinatesBusy(drone, matrix);
        verify(droneService).coordinatesOutOfMatrix(drone, matrix);
    }

    @Test
    void getDroneByCoordinates() {
        when(droneRepository.findByCoordinates(1L, 2, 2)).thenReturn(Optional.of(drone));
        when(droneMapper.mapDroneToDroneDTO(drone)).thenReturn(droneDTO);
        DroneDTO result = droneService.getDroneByCoordinates(1L, 2, 2);

        assertEquals(droneDTO, result);
    }

    @Test
    void getDroneByCoordinatesWrongCooRdinates() {
        when(droneRepository.findByCoordinates(1L, 3, 2)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            droneService.getDroneByCoordinates(1L, 3, 2);
        });

        verify(droneRepository).findByCoordinates(1L, 3, 2);
    }

    @Test
    void moveOneDrone() {
        matrix.setDrones(List.of(drone));
        DroneMoveDTO droneMoveDTO = new DroneMoveDTO(1L, 1L, List.of(Movimientos.MOVE_FORWARD));
        when(droneRepository.findById(1L)).thenReturn(Optional.of(drone));
        when(matrixService.getMatrixById(droneNoIdDTO.getMatrizId())).thenReturn(matrix);
        when(droneRepository.save(drone)).thenReturn(drone);

        droneService.moveOneDrone(droneMoveDTO);
        verify(droneRepository).save(drone);
        verify(droneService).executeOrders(droneMoveDTO.getOrden(), drone, matrix);

    }

    @Test
    void moveManyInMatrix() {
        DroneEntradaDTO drone1 = new DroneEntradaDTO(1L, "Drone1", "ModeloA", 0, 0, "N", List.of(Movimientos.MOVE_FORWARD));
        DroneEntradaDTO drone2 = new DroneEntradaDTO(2L, "Drone2", "ModeloB", 1, 1, "N", List.of(Movimientos.MOVE_FORWARD));
        List<DroneEntradaDTO> dronesEntrada = List.of(drone1, drone2);
        DatosEntradaDTO datosEntradaDTO = new DatosEntradaDTO(new MatrixEntradaDTO(), dronesEntrada);

        Drone droneEntity1 = new Drone(1L, "Drone1", "ModeloA", 0, 0, Orientacion.N, matrix);
        Drone droneEntity2 = new Drone(2L, "Drone2", "ModeloB", 1, 1, Orientacion.N, matrix);

        when(matrixService.getMatrixById(1L)).thenReturn(matrix);
        when(droneMapper.mapDroneEntradaDTOToDrone(drone1)).thenReturn(droneEntity1);
        when(droneMapper.mapDroneEntradaDTOToDrone(drone2)).thenReturn(droneEntity2);

        Matrix result = droneService.moveManyInMatrix(datosEntradaDTO, 1L);

        verify(matrixService).getMatrixById(1L);
        verify(droneMapper).mapDroneEntradaDTOToDrone(drone1);
        verify(droneMapper).mapDroneEntradaDTOToDrone(drone2);
        verify(droneService).executeOrders(drone1.getOrden(), droneEntity1, matrix);
        verify(droneService).executeOrders(drone2.getOrden(), droneEntity2, matrix);

        assertEquals(matrix, result);
    }


    @ParameterizedTest
    @CsvSource({
            "O, 1",
            "E, 3"
    })
    void moveForwardX(String orientation, int result) {
        drone.setOrientacion(Orientacion.valueOf(orientation));
        droneService.moveForward(drone, matrix);
        assertEquals(result, drone.getX());
        verify(droneService).coordinatesOutOfMatrix(drone, matrix);
        verify(droneService).coordinatesBusy(drone, matrix);
    }

    @ParameterizedTest
    @CsvSource({
            "S, 1",
            "N, 3"
    })
    void moveForwardY(String orientation, int result) {
        drone.setOrientacion(Orientacion.valueOf(orientation));
        droneService.moveForward(drone, matrix);
        assertEquals(result, drone.getY());
        verify(droneService).coordinatesOutOfMatrix(drone, matrix);
        verify(droneService).coordinatesBusy(drone, matrix);
    }

    @Test
    void coordinatesOutOfMatrix() {
        droneService.coordinatesOutOfMatrix(drone, matrix);
    }

    @Test
    void coordinatesOutOfMatrixWrongCoordinates() {
        drone.setX(6);
        WrongCoordinatesException exception = assertThrows(WrongCoordinatesException.class, () -> {
            droneService.coordinatesOutOfMatrix(drone, matrix);
        });

        assertEquals("La coordenada excede el límite de la matriz", exception.getMessage());
    }

    @Test
    void coordinatesBusy() {
        droneService.coordinatesBusy(drone, matrix);
    }

    @Test
    void coordinatesBusyWrongCoordinates() {
        Drone drone2 = new Drone(2L, "Test", "Test", 2, 2, Orientacion.N, null);
        matrix.setDrones(List.of(drone));
        WrongCoordinatesException exception = assertThrows(WrongCoordinatesException.class, () -> {
            droneService.coordinatesBusy(drone2, matrix);
        });

        assertEquals("Coordenadas ocupadas por el dron con el ID 1", exception.getMessage());
    }

    @Test
    void executeOrders() {
        List<Movimientos> movementList = List.of(Movimientos.MOVE_FORWARD, Movimientos.TURN_LEFT, Movimientos.TURN_RIGHT, Movimientos.TURN_RIGHT);
        when(droneRepository.save(drone)).thenReturn(drone);
        droneService.executeOrders(movementList, drone, matrix);
        assertEquals(Orientacion.E, drone.getOrientacion());
    }

}