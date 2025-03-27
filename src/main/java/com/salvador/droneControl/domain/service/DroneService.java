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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class DroneService {

    private static final Logger logger = LoggerFactory.getLogger(DroneService.class);
    private final DroneRepository droneRepository;
    private final DroneMapper droneMapper;
    private final MatrixService matrixService;

    @Autowired
    public DroneService(DroneRepository droneRepository, DroneMapper droneMapper, MatrixService matrixService) {
        this.droneRepository = droneRepository;
        this.droneMapper = droneMapper;
        this.matrixService = matrixService;
    }

    public Drone getDroneEntityById(long id) {
        return droneRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Drone no encontrado con id: " + id));
    }

    public Drone saveDroneEntity(Drone drone) {
        return droneRepository.save(drone);
    }

    public DroneDTO deleteDroneEntityById(long id) {
        Drone drone = this.getDroneEntityById(id);
        droneRepository.delete(drone);
        return droneMapper.mapDroneEntityToDrone(drone);
    }

    public DroneDTO createDrone(DroneNoIdDTO droneNoIdDTO) {
        Matrix matrix = matrixService.getMatrixEntityById(droneNoIdDTO.getMatrizId());
        Drone drone = droneMapper.mapDroneDTOToEntity(droneNoIdDTO);

        //Comprobamos que el dron que creamos esté dentro de la matriz y que no esté en las mismas coordenadas
        //que otro dron
        coordinatesOutOfMatrix(drone, matrix);
        coordinatesBusy(drone, matrix);

        drone.setId(null);
        drone.setMatriz(matrix);
        Drone createdDrone = this.saveDroneEntity(drone);
        return droneMapper.mapDroneEntityToDrone(createdDrone);
    }

    public DroneDTO updateDrone(DroneNoIdDTO droneNoIdDTO, long id) {
        Drone oldDrone = this.getDroneEntityById(id);
        Matrix matrix = matrixService.getMatrixEntityById(droneNoIdDTO.getMatrizId());
        coordinatesOutOfMatrix(droneMapper.mapDroneDTOToEntity(droneNoIdDTO), matrix);
        coordinatesBusy(droneMapper.mapDroneDTOToEntity(droneNoIdDTO), matrix);

        Drone mapUpdatedDrone = droneMapper.mapUpdateDroneDTOToEntity(droneNoIdDTO, oldDrone, matrix);
        Drone updatedDrone = this.saveDroneEntity(mapUpdatedDrone);
        return droneMapper.mapDroneEntityToDrone(updatedDrone);
    }

    public DroneDTO getDroneByCoordinates(long matrizId, int x, int y) {
        Drone drone = droneRepository.findByCoordinates(matrizId, x, y).orElseThrow(() ->
                new ResourceNotFoundException("Drone no encontrado en la matriz en las coordenadas x= " +
                        x + ", y= " + y));
        return droneMapper.mapDroneEntityToDrone(drone);
    }

    public Matrix moveOneDrone(DroneMoveDTO droneMoveDTO) {
        Drone drone = this.getDroneEntityById(droneMoveDTO.getId());
        Matrix matrix = matrixService.getMatrixEntityById(droneMoveDTO.getMatrizId());
        executeOrders(droneMoveDTO.getOrden(), drone, matrix);
        return matrix;
    }

    public Matrix moveManyInMatrix(DatosEntradaDTO datosEntradaDTO, long id) {
        Matrix matrix = matrixService.getMatrixEntityById(id);
        for (DroneEntradaDTO drone : datosEntradaDTO.getDrones()) {
            Drone droneEntity = droneMapper.mapEntradaDTOToEntity(drone);
            droneEntity.setMatriz(matrix);
            executeOrders(drone.getOrden(), droneEntity, matrix);
        }
        return matrix;
    }

    // Métodos auxiliares
    private void coordinatesOutOfMatrix(Drone drone, Matrix matrix) {
        if (drone.getX() > matrix.getMax_x() || drone.getY() > matrix.getMax_y() ||
                drone.getX() < 0 || drone.getY() < 0) {
            String errorMessage = "La coordenada excede el límite de la matriz";
            logger.error(errorMessage);
            throw new WrongCoordinatesException(errorMessage);
        }
    }

    private void coordinatesBusy(Drone droneMoving, Matrix matrix) {
        List<Drone> drones = matrix.getDrones();
        for (Drone drone : drones) {
            if (!Objects.equals(drone.getId(), droneMoving.getId()) && drone.getX() == droneMoving.getX() && drone.getY() == droneMoving.getY()) {
                String errorMessage = "Coordenadas ocupadas por el dron con el ID " + drone.getId();
                logger.error(errorMessage);
                throw new WrongCoordinatesException(errorMessage);
            }
        }
    }

    private void executeOrders(Movimientos[] ordenes, Drone drone, Matrix matrix) {
        for (Movimientos orden : ordenes) {
            switch (orden) {
                case MOVE_FORWARD:
                    moveForward(drone, matrix);
                    break;
                case TURN_LEFT:
                    drone.setOrientacion(drone.getOrientacion().turnLeft());
                    break;
                case TURN_RIGHT:
                    drone.setOrientacion(drone.getOrientacion().turnRight());
                    break;
            }
            this.saveDroneEntity(drone);
        }
    }

    public void moveForward(Drone drone, Matrix matrix) {
        switch (drone.getOrientacion()) {
            case Orientacion.N:
                drone.setY(drone.getY() + 1);
                break;
            case Orientacion.S:
                drone.setY(drone.getY() - 1);
                break;
            case Orientacion.E:
                drone.setX(drone.getX() + 1);
                break;
            case Orientacion.O:
                drone.setX(drone.getX() - 1);
                break;
        }
        coordinatesBusy(drone, matrix);
        coordinatesOutOfMatrix(drone, matrix);
    }
}
