package com.salvador.droneControl.infrastructure.controller;

import com.salvador.droneControl.application.dto.*;
import com.salvador.droneControl.application.mapper.DroneMapper;
import com.salvador.droneControl.application.service.DroneService;
import com.salvador.droneControl.application.service.MatrixService;
import com.salvador.droneControl.domain.model.Movimientos;
import com.salvador.droneControl.domain.model.Orientacion;
import com.salvador.droneControl.infrastructure.exception.ResourceNotFoundException;
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
import java.util.Map;
import java.util.Optional;

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
        Optional<MatrixEntity> optMatrixEntity = matrixService.getMatrixEntityById(droneDTO.getMatrizId());
        if (optMatrixEntity.isPresent()) {
            MatrixEntity matrixEntity = optMatrixEntity.get();

            //Comprobamos que el dron que creamos esté dentro de la matriz y que no esté en las mismas coordenadas
            //que otro dron
            coordinatesOutOfMatrix(droneDTO, matrixEntity);
            coordinatesBusy(droneDTO, matrixEntity);

            droneEntity.setMatriz(matrixEntity);
            DroneEntity createdDroneEntity = droneService.saveDroneEntity(droneEntity);
            droneDTO.setId(createdDroneEntity.getId());
            return new ResponseEntity<>(droneDTO, HttpStatus.CREATED);
        } else {
            logger.error("Matriz no encontrada con id: {}", droneDTO.getMatrizId());
            throw new ResourceNotFoundException("Matriz no encontrada con id: " + droneDTO.getMatrizId());
        }
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<DroneEntity> updateDrone(@RequestBody @Valid DroneDTO droneDTO, @PathVariable Long id) {
        logger.info("Actualizando dron");
        Optional<DroneEntity> optOldDroneEntity = droneService.getDroneById(id);
        Optional<MatrixEntity> optMatrixEntity = matrixService.getMatrixEntityById(droneDTO.getMatrizId());
        if (optOldDroneEntity.isPresent() && optMatrixEntity.isPresent()) {
            logger.info("Dron y matriz existen");
            MatrixEntity matrixEntity = optMatrixEntity.get();
            coordinatesOutOfMatrix(droneDTO, matrixEntity);

            DroneEntity oldDroneEntity = optOldDroneEntity.get();
            oldDroneEntity.setNombre(droneDTO.getNombre());
            oldDroneEntity.setModelo(droneDTO.getModelo());
            oldDroneEntity.setX(droneDTO.getX());
            oldDroneEntity.setY(droneDTO.getY());
            oldDroneEntity.setOrientacion(Orientacion.valueOf(droneDTO.getOrientacion()));
            oldDroneEntity.setMatriz(matrixEntity);

            droneService.saveDroneEntity(oldDroneEntity);
            return new ResponseEntity<>(oldDroneEntity, HttpStatus.OK);
        }
        logger.error("Dron no existente");
        throw new ResourceNotFoundException("Drone no encontrado con id: " + id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DroneEntity> deleteDrone(@PathVariable Long id) {
        logger.info("Borrando dron");
        Optional<DroneEntity> optDroneEntity = droneService.getDroneById(id);
        if (optDroneEntity.isPresent()) {
            DroneEntity droneEntity = optDroneEntity.get();
            DroneEntity deletedDrone = droneService.deleteDroneEntity(droneEntity);
            return new ResponseEntity<>(deletedDrone, HttpStatus.OK);
        } else {
            logger.error("El dron no existe");
            throw new ResourceNotFoundException("Drone no encontrado con id: " + id);
        }
    }

    @PostMapping("/findByCoordinates")
    public ResponseEntity<DroneEntity> findByCoordinates(@RequestBody @Valid DroneCoordinatesDTO droneCoordinatesDTO) {
        logger.info("Buscando dron por coordenadas x={} y={}", droneCoordinatesDTO.getX(), droneCoordinatesDTO.getY());
        Optional<DroneEntity> droneEntity = droneService.getDroneEntityByCoordinates(
                droneCoordinatesDTO.getMatriz_id(), droneCoordinatesDTO.getX(), droneCoordinatesDTO.getY());
        if (droneEntity.isPresent()) {
            return new ResponseEntity<>(droneEntity.get(), HttpStatus.OK);
        } else {
            logger.error("El dron no existe");
            throw new ResourceNotFoundException("Drone no encontrado en la matriz en las coordenadas x=" + droneCoordinatesDTO.getX() + ", y=" + droneCoordinatesDTO.getY());
        }
    }

    @PostMapping("/move")
    public ResponseEntity<MatrixEntity> moveOne(@RequestBody @Valid DroneMoveDTO droneMoveDTO) {
        logger.info("Moviendo dron con id: {}", droneMoveDTO.getId());
        Optional<DroneEntity> droneEntity = droneService.getDroneById(droneMoveDTO.getId());
        Optional<MatrixEntity> matrixEntity = matrixService.getMatrixEntityById(droneMoveDTO.getMatrizId());
        if (droneEntity.isPresent() && matrixEntity.isPresent()) {
            DroneEntity drone = droneEntity.get();

            executeOrders(droneMoveDTO.getOrden(), drone, matrixEntity.get());
            return new ResponseEntity<>(matrixEntity.get(), HttpStatus.OK);
        } else {
            logger.error("Dron o matriz no existente");
            throw new ResourceNotFoundException("Drone no encontrado con id: " + droneMoveDTO.getId() +
                    " o matriz no encontrada con id: " + droneMoveDTO.getMatrizId());
        }
    }

    @PostMapping("/moveManyInMatrix/{id}")
    public ResponseEntity<MatrixEntity> moveMany(@RequestBody @Valid DatosEntradaDTO datosEntradaDTO, @PathVariable Long id) {
        logger.info("Moviendo varios drones");
        Optional<MatrixEntity> matrixEntity = matrixService.getMatrixEntityById(id);
        if (matrixEntity.isPresent()) {

            for (DroneEntradaDTO drone : datosEntradaDTO.getDrones()) {
                DroneEntity droneEntity = droneMapper.mapEntradaDTOToEntity(drone);
                droneEntity.setMatriz(matrixEntity.get());
                executeOrders(drone.getOrden(), droneEntity, matrixEntity.get());
            }

            return new ResponseEntity<>(matrixEntity.get(), HttpStatus.OK);
        } else {
            logger.error("Dron o matriz no existente");
            throw new ResourceNotFoundException("Matriz no encontrada con id: " + id);
        }
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
                    switch (drone.getOrientacion()) {
                        case Orientacion.N:
                            drone.setOrientacion(Orientacion.O);
                            break;
                        case Orientacion.S:
                            drone.setOrientacion(Orientacion.E);
                            break;
                        case Orientacion.E:
                            drone.setOrientacion(Orientacion.N);
                            break;
                        case Orientacion.O:
                            drone.setOrientacion(Orientacion.S);
                            break;
                    }
                    break;
                case TURN_RIGHT:
                    switch (drone.getOrientacion()) {
                        case Orientacion.N:
                            drone.setOrientacion(Orientacion.E);
                            break;
                        case Orientacion.S:
                            drone.setOrientacion(Orientacion.O);
                            break;
                        case Orientacion.E:
                            drone.setOrientacion(Orientacion.S);
                            break;
                        case Orientacion.O:
                            drone.setOrientacion(Orientacion.N);
                            break;
                    }
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
