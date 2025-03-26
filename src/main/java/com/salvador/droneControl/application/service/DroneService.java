package com.salvador.droneControl.application.service;

import com.salvador.droneControl.application.dto.DatosEntradaDTO;
import com.salvador.droneControl.application.dto.DroneDTO;
import com.salvador.droneControl.application.dto.DroneEntradaDTO;
import com.salvador.droneControl.application.dto.DroneMoveDTO;
import com.salvador.droneControl.application.mapper.DroneMapper;
import com.salvador.droneControl.domain.model.Drone;
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

    public DroneEntity getDroneEntityById(long id) {
        return droneRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Drone no encontrado con id: " + id));
    }

    public DroneEntity saveDroneEntity(DroneEntity droneEntity) {
        return droneRepository.save(droneEntity);
    }

    public Drone deleteDroneEntityById(long id) {
        DroneEntity droneEntity = this.getDroneEntityById(id);
        droneRepository.delete(droneEntity);
        return droneMapper.mapDroneEntityToDrone(droneEntity);
    }

    public Drone createDrone(DroneDTO droneDTO) {
        MatrixEntity matrixEntity = matrixService.getMatrixEntityById(droneDTO.getMatrizId());
        DroneEntity droneEntity = droneMapper.mapDroneDTOToEntity(droneDTO);

        //Comprobamos que el dron que creamos esté dentro de la matriz y que no esté en las mismas coordenadas
        //que otro dron
        coordinatesOutOfMatrix(droneEntity, matrixEntity);
        coordinatesBusy(droneEntity, matrixEntity);

        droneEntity.setId(null);
        droneEntity.setMatriz(matrixEntity);
        DroneEntity createdDroneEntity = this.saveDroneEntity(droneEntity);
        return droneMapper.mapDroneEntityToDrone(createdDroneEntity);
    }

    public Drone updateDrone(DroneDTO droneDTO, long id) {
        DroneEntity oldDroneEntity = this.getDroneEntityById(id);
        MatrixEntity matrixEntity = matrixService.getMatrixEntityById(droneDTO.getMatrizId());
        coordinatesOutOfMatrix(droneMapper.mapDroneDTOToEntity(droneDTO), matrixEntity);
        coordinatesBusy(droneMapper.mapDroneDTOToEntity(droneDTO), matrixEntity);

        DroneEntity mapUpdatedDroneEntity = droneMapper.mapUpdateDroneDTOToEntity(droneDTO, oldDroneEntity, matrixEntity);
        DroneEntity updatedDroneEntity = this.saveDroneEntity(mapUpdatedDroneEntity);
        return droneMapper.mapDroneEntityToDrone(updatedDroneEntity);
    }

    public Drone getDroneByCoordinates(long matrizId, int x, int y) {
        DroneEntity droneEntity = droneRepository.findByCoordinates(matrizId, x, y).orElseThrow(() ->
                new ResourceNotFoundException("Drone no encontrado en la matriz en las coordenadas x= " +
                        x + ", y= " + y));
        return droneMapper.mapDroneEntityToDrone(droneEntity);
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
    private void coordinatesOutOfMatrix(DroneEntity droneEntity, MatrixEntity matrixEntity) {
        if (droneEntity.getX() > matrixEntity.getMax_x() || droneEntity.getY() > matrixEntity.getMax_y() ||
                droneEntity.getX() < 0 || droneEntity.getY() < 0) {
            String errorMessage = "La coordenada excede el límite de la matriz";
            logger.error(errorMessage);
            throw new WrongCoordinatesException(errorMessage);
        }
    }

    private void coordinatesBusy(DroneEntity droneMoving, MatrixEntity matrixEntity) {
        List<DroneEntity> drones = matrixEntity.getDrones();
        for (DroneEntity drone : drones) {
            if (!Objects.equals(drone.getId(), droneMoving.getId()) && drone.getX() == droneMoving.getX() && drone.getY() == droneMoving.getY()) {
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
        coordinatesBusy(drone, matrixEntity);
        coordinatesOutOfMatrix(drone, matrixEntity);
    }
}
