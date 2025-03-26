package com.salvador.droneControl.application.service;

import com.salvador.droneControl.application.dto.DatosEntradaDTO;
import com.salvador.droneControl.application.dto.DroneDTO;
import com.salvador.droneControl.application.dto.DroneEntradaDTO;
import com.salvador.droneControl.application.dto.DroneMoveDTO;
import com.salvador.droneControl.application.mapper.DroneMapper;
import com.salvador.droneControl.domain.model.Movimientos;
import com.salvador.droneControl.domain.model.Orientacion;
import com.salvador.droneControl.infrastructure.exception.ResourceNotFoundException;
import com.salvador.droneControl.infrastructure.exception.WrongCoordinatesException;
import com.salvador.droneControl.infrastructure.persistence.entity.DroneEntity;
import com.salvador.droneControl.infrastructure.persistence.entity.MatrixEntity;
import com.salvador.droneControl.infrastructure.persistence.repository.DroneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public DroneEntity getDroneEntityById(long id) {
        return droneRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Drone no encontrado con id: " + id));
    }

    public DroneEntity saveDroneEntity(DroneEntity droneEntity) {
        return droneRepository.save(droneEntity);
    }

    public DroneEntity deleteDroneEntityById(long id) {
        DroneEntity droneEntity = this.getDroneEntityById(id);
        droneRepository.delete(droneEntity);
        return droneEntity;
    }

    public DroneDTO createDrone(DroneDTO droneDTO) {
        DroneEntity droneEntity = droneMapper.mapToEntity(droneDTO);
        MatrixEntity matrixEntity = matrixService.getMatrixEntityById(droneDTO.getMatrizId());

        //Comprobamos que el dron que creamos esté dentro de la matriz y que no esté en las mismas coordenadas
        //que otro dron
        coordinatesOutOfMatrix(droneDTO, matrixEntity);
        coordinatesBusy(droneDTO, matrixEntity);

        droneEntity.setMatriz(matrixEntity);
        DroneEntity createdDroneEntity = this.saveDroneEntity(droneEntity);
        droneDTO.setId(createdDroneEntity.getId());
        return droneDTO;
    }

    public DroneEntity updateDrone(DroneDTO droneDTO, long id) {
        DroneEntity oldDroneEntity = this.getDroneEntityById(id);
        MatrixEntity matrixEntity = matrixService.getMatrixEntityById(droneDTO.getMatrizId());

        coordinatesOutOfMatrix(droneDTO, matrixEntity);
        coordinatesBusy(droneDTO, matrixEntity);
        DroneEntity updatedDrone = droneMapper.mapUpdateDroneDTOToEntity(droneDTO, oldDroneEntity, matrixEntity);
        return this.saveDroneEntity(updatedDrone);
    }

    public DroneEntity getDroneEntityByCoordinates(long matrizId, int x, int y) {

        return droneRepository.findByCoordinates(matrizId, x, y).orElseThrow(() ->
                new ResourceNotFoundException("Drone no encontrado en la matriz en las coordenadas x= " +
                        x + ", y= " + y));
    }

    public MatrixEntity moveOneDrone(DroneMoveDTO droneMoveDTO) {
        DroneEntity droneEntity = this.getDroneEntityById(droneMoveDTO.getId());
        MatrixEntity matrixEntity = matrixService.getMatrixEntityById(droneMoveDTO.getMatrizId());
        executeOrders(droneMoveDTO.getOrden(), droneEntity, matrixEntity);
        return matrixEntity;
    }

    public MatrixEntity moveManyInMatrix(DatosEntradaDTO datosEntradaDTO, long id) {
        MatrixEntity matrixEntity = matrixService.getMatrixEntityById(id);
        for (DroneEntradaDTO drone : datosEntradaDTO.getDrones()) {
            DroneEntity droneEntity = droneMapper.mapEntradaDTOToEntity(drone);
            droneEntity.setMatriz(matrixEntity);
            executeOrders(drone.getOrden(), droneEntity, matrixEntity);
        }
        return matrixEntity;
    }

    // Métodos auxiliares
    private void coordinatesOutOfMatrix(DroneDTO droneDTO, MatrixEntity matrixEntity) {
        if (droneDTO.getX() > matrixEntity.getMax_x() || droneDTO.getY() > matrixEntity.getMax_y()) {
            String errorMessage = "La coordenada excede el límite de la matriz";
            logger.error(errorMessage);
            throw new WrongCoordinatesException(errorMessage);
        }
    }

    private void coordinatesBusy(DroneDTO droneDTO, MatrixEntity matrixEntity) {
        List<DroneEntity> drones = matrixEntity.getDrones();
        for (DroneEntity drone : drones) {
            if (drone.getX() == droneDTO.getX() && drone.getY() == droneDTO.getY()) {
                String errorMessage = "Coordenadas ocupadas por el dron con el ID " + drone.getId();
                logger.error(errorMessage);
                throw new WrongCoordinatesException(errorMessage);
            }
        }
    }

    private void executeOrders(Movimientos[] ordenes, DroneEntity drone, MatrixEntity matrixEntity) {
        for (Movimientos orden : ordenes) {
            switch (orden) {
                case MOVE_FORWARD:
                    moveForward(drone, matrixEntity);
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

    public void moveForward(DroneEntity drone, MatrixEntity matrixEntity) {
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
        coordinatesBusy(droneMapper.mapToDTO(drone), matrixEntity);
        coordinatesOutOfMatrix(droneMapper.mapToDTO(drone), matrixEntity);
    }
}
