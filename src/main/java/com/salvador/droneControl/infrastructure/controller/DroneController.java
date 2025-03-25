package com.salvador.droneControl.infrastructure.controller;

import com.salvador.droneControl.application.dto.*;
import com.salvador.droneControl.application.mapper.DroneMapper;
import com.salvador.droneControl.application.service.DroneService;
import com.salvador.droneControl.application.service.MatrixService;
import com.salvador.droneControl.domain.model.Movimientos;
import com.salvador.droneControl.domain.model.Orientacion;
import com.salvador.droneControl.infrastructure.exception.WrongCoordinatesException;
import com.salvador.droneControl.infrastructure.persistence.entity.DroneEntity;
import com.salvador.droneControl.infrastructure.persistence.entity.MatrixEntity;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/drone")
public class DroneController {

    private static final Logger logger = LoggerFactory.getLogger(DroneController.class);
    private final DroneService droneService;
    private final DroneMapper droneMapper;
    private final MatrixService matrixService;

    @Autowired
    public DroneController(DroneService droneService, DroneMapper droneMapper, MatrixService matrixService) {
        this.droneService = droneService;
        this.droneMapper = droneMapper;
        this.matrixService = matrixService;
    }

    @PostMapping("/new")
    public ResponseEntity<DroneDTO> newDrone(@RequestBody @Valid DroneDTO droneDTO) {
        logger.info("Creando dron");
        DroneEntity droneEntity = droneMapper.mapToEntity(droneDTO);
        MatrixEntity matrixEntity = matrixService.getMatrixEntityById(droneDTO.getMatrizId());

        //Comprobamos que el dron que creamos esté dentro de la matriz y que no esté en las mismas coordenadas
        //que otro dron
        coordinatesOutOfMatrix(droneDTO, matrixEntity);
        coordinatesBusy(droneDTO, matrixEntity);

        droneEntity.setMatriz(matrixEntity);
        DroneEntity createdDroneEntity = droneService.saveDroneEntity(droneEntity);
        droneDTO.setId(createdDroneEntity.getId());
        return new ResponseEntity<>(droneDTO, HttpStatus.CREATED);

    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<DroneEntity> updateDrone(@RequestBody @Valid DroneDTO droneDTO, @PathVariable Long id) {
        logger.info("Actualizando dron");
        DroneEntity oldDroneEntity = droneService.getDroneEntityById(id);
        MatrixEntity matrixEntity = matrixService.getMatrixEntityById(droneDTO.getMatrizId());

        coordinatesOutOfMatrix(droneDTO, matrixEntity);

        oldDroneEntity.setNombre(droneDTO.getNombre());
        oldDroneEntity.setModelo(droneDTO.getModelo());
        oldDroneEntity.setX(droneDTO.getX());
        oldDroneEntity.setY(droneDTO.getY());
        oldDroneEntity.setOrientacion(Orientacion.valueOf(droneDTO.getOrientacion()));
        oldDroneEntity.setMatriz(matrixEntity);

        droneService.saveDroneEntity(oldDroneEntity);
        return new ResponseEntity<>(oldDroneEntity, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DroneEntity> deleteDrone(@PathVariable Long id) {
        logger.info("Borrando dron");
        DroneEntity droneEntity = droneService.getDroneEntityById(id);
        DroneEntity deletedDrone = droneService.deleteDroneEntity(droneEntity);
        return new ResponseEntity<>(deletedDrone, HttpStatus.OK);

    }

    @PostMapping("/findByCoordinates")
    public ResponseEntity<DroneEntity> findByCoordinates(@RequestBody @Valid DroneCoordinatesDTO droneCoordinatesDTO) {
        logger.info("Buscando dron por coordenadas x={} y={}", droneCoordinatesDTO.getX(), droneCoordinatesDTO.getY());
        DroneEntity droneEntity = droneService.getDroneEntityByCoordinates(
                droneCoordinatesDTO.getMatriz_id(), droneCoordinatesDTO.getX(), droneCoordinatesDTO.getY());
        return new ResponseEntity<>(droneEntity, HttpStatus.OK);
    }

    @PostMapping("/move")
    public ResponseEntity<MatrixEntity> moveOne(@RequestBody @Valid DroneMoveDTO droneMoveDTO) {
        logger.info("Moviendo dron con id: {}", droneMoveDTO.getId());
        DroneEntity droneEntity = droneService.getDroneEntityById(droneMoveDTO.getId());
        MatrixEntity matrixEntity = matrixService.getMatrixEntityById(droneMoveDTO.getMatrizId());

        executeOrders(droneMoveDTO.getOrden(), droneEntity, matrixEntity);
        return new ResponseEntity<>(matrixEntity, HttpStatus.OK);

    }

    @PostMapping("/moveManyInMatrix/{id}")
    public ResponseEntity<MatrixEntity> moveMany(@RequestBody @Valid DatosEntradaDTO datosEntradaDTO, @PathVariable Long id) {
        logger.info("Moviendo varios drones");
        MatrixEntity matrixEntity = matrixService.getMatrixEntityById(id);
        for (DroneEntradaDTO drone : datosEntradaDTO.getDrones()) {
            DroneEntity droneEntity = droneMapper.mapEntradaDTOToEntity(drone);
            droneEntity.setMatriz(matrixEntity);
            executeOrders(drone.getOrden(), droneEntity, matrixEntity);
        }
        return new ResponseEntity<>(matrixEntity, HttpStatus.OK);

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
            droneService.saveDroneEntity(drone);
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
