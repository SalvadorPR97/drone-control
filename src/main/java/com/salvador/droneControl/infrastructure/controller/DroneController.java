package com.salvador.droneControl.infrastructure.controller;

import com.salvador.droneControl.application.dto.DatosEntradaDTO;
import com.salvador.droneControl.application.dto.DroneCoordinatesDTO;
import com.salvador.droneControl.application.dto.DroneDTO;
import com.salvador.droneControl.application.dto.DroneMoveDTO;
import com.salvador.droneControl.application.service.DroneService;
import com.salvador.droneControl.domain.model.Drone;
import com.salvador.droneControl.infrastructure.persistence.entity.MatrixEntity;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/drone")
public class DroneController {

    private static final Logger logger = LoggerFactory.getLogger(DroneController.class);
    private final DroneService droneService;

    @Autowired
    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    @PostMapping("/new")
    public ResponseEntity<Drone> newDrone(@RequestBody @Valid DroneDTO droneDTO) {
        logger.info("Creando dron");
        Drone newDrone = droneService.createDrone(droneDTO);
        return new ResponseEntity<>(newDrone, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Drone> updateDrone(@RequestBody @Valid DroneDTO droneDTO, @PathVariable Long id) {
        logger.info("Actualizando dron");
        Drone updatedDrone = droneService.updateDrone(droneDTO, id);
        return new ResponseEntity<>(updatedDrone, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Drone> deleteDrone(@PathVariable Long id) {
        logger.info("Borrando dron");
        Drone deletedDrone = droneService.deleteDroneEntityById(id);
        return new ResponseEntity<>(deletedDrone, HttpStatus.OK);
    }

    @PostMapping("/findByCoordinates")
    public ResponseEntity<Drone> findByCoordinates(@RequestBody @Valid DroneCoordinatesDTO droneCoordinatesDTO) {
        logger.info("Buscando dron por coordenadas x={} y={}", droneCoordinatesDTO.getX(), droneCoordinatesDTO.getY());
        Drone drone = droneService.getDroneByCoordinates(
                droneCoordinatesDTO.getMatriz_id(), droneCoordinatesDTO.getX(), droneCoordinatesDTO.getY());
        return new ResponseEntity<>(drone, HttpStatus.OK);
    }

    @PostMapping("/move")
    public ResponseEntity<MatrixEntity> moveOne(@RequestBody @Valid DroneMoveDTO droneMoveDTO) {
        logger.info("Moviendo dron con id: {}", droneMoveDTO.getId());
        MatrixEntity matrizResultante = droneService.moveOneDrone(droneMoveDTO);
        return new ResponseEntity<>(matrizResultante, HttpStatus.OK);
    }

    @PostMapping("/moveManyInMatrix/{id}")
    public ResponseEntity<MatrixEntity> moveMany(@RequestBody @Valid DatosEntradaDTO datosEntradaDTO, @PathVariable Long id) {
        logger.info("Moviendo varios drones");
        MatrixEntity matrixEntity = droneService.moveManyInMatrix(datosEntradaDTO, id);
        return new ResponseEntity<>(matrixEntity, HttpStatus.OK);
    }

}