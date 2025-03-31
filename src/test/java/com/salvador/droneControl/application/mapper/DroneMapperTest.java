package com.salvador.droneControl.application.mapper;

import com.salvador.droneControl.application.dto.DroneDTO;
import com.salvador.droneControl.application.dto.DroneEntradaDTO;
import com.salvador.droneControl.application.dto.DroneNoIdDTO;
import com.salvador.droneControl.domain.model.Drone;
import com.salvador.droneControl.domain.model.Matrix;
import com.salvador.droneControl.domain.model.Movimientos;
import com.salvador.droneControl.domain.model.Orientacion;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class DroneMapperTest {

    private DroneMapper droneMapper;

    @BeforeEach
    void setUp() {
        this.droneMapper = new DroneMapper();
    }

    @Test
    void mapDroneToDroneDTO() {
        Matrix matrix = new Matrix();
        Drone drone = new Drone(1L, "Test", "Test", 0, 0, Orientacion.N, matrix);

        DroneDTO mappedDroneDTO = droneMapper.mapDroneToDroneDTO(drone);
        assertEquals(drone.getId(), mappedDroneDTO.getId());
        assertEquals(drone.getNombre(), mappedDroneDTO.getNombre());
        assertEquals(drone.getModelo(), mappedDroneDTO.getModelo());
        assertEquals(drone.getX(), mappedDroneDTO.getX());
        assertEquals(drone.getY(), mappedDroneDTO.getY());
        assertEquals(drone.getOrientacion(), mappedDroneDTO.getOrientacion());
    }

    @Test
    void mapDroneNoIdDTOToDrone() {
        DroneNoIdDTO droneNoIdDTO = new DroneNoIdDTO("Test", "Test", 0, 0, "N", 1L);

        Drone mappedDrone = droneMapper.mapDroneNoIdDTOToDrone(droneNoIdDTO);
        assertEquals(droneNoIdDTO.getNombre(), mappedDrone.getNombre());
        assertEquals(droneNoIdDTO.getModelo(), mappedDrone.getModelo());
        assertEquals(droneNoIdDTO.getX(), mappedDrone.getX());
        assertEquals(droneNoIdDTO.getY(), mappedDrone.getY());
        assertEquals(droneNoIdDTO.getOrientacion(), mappedDrone.getOrientacion().toString());
    }

    @Test
    void mapDroneEntradaDTOToDrone() {
        List<Movimientos> movesList = new ArrayList<>();
        movesList.add(Movimientos.TURN_LEFT);
        movesList.add(Movimientos.TURN_RIGHT);
        DroneEntradaDTO droneEntradaDTO =
                new DroneEntradaDTO(1L, "Test", "Test", 0, 0, "N", movesList);

        Drone mappedDrone = droneMapper.mapDroneEntradaDTOToDrone(droneEntradaDTO);
        assertEquals(droneEntradaDTO.getId(), mappedDrone.getId());
        assertEquals(droneEntradaDTO.getNombre(), mappedDrone.getNombre());
        assertEquals(droneEntradaDTO.getModelo(), mappedDrone.getModelo());
        assertEquals(droneEntradaDTO.getX(), mappedDrone.getX());
        assertEquals(droneEntradaDTO.getY(), mappedDrone.getY());
        assertEquals(droneEntradaDTO.getOrientacion(), mappedDrone.getOrientacion().toString());
    }
}