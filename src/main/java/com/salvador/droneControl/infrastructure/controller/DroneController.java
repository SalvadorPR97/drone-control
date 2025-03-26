package com.salvador.droneControl.infrastructure.controller;

import com.salvador.droneControl.application.dto.DatosEntradaDTO;
import com.salvador.droneControl.application.dto.DroneCoordinatesDTO;
import com.salvador.droneControl.application.dto.DroneDTO;
import com.salvador.droneControl.application.dto.DroneMoveDTO;
import com.salvador.droneControl.application.service.DroneService;
import com.salvador.droneControl.infrastructure.persistence.entity.DroneEntity;
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
    public ResponseEntity<DroneDTO> newDrone(@RequestBody @Valid DroneDTO droneDTO) {
        logger.info("Creando dron");
        DroneDTO newDrone = droneService.createDrone(droneDTO);
        return new ResponseEntity<>(newDrone, HttpStatus.CREATED);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<DroneEntity> updateDrone(@RequestBody @Valid DroneDTO droneDTO, @PathVariable Long id) {
        logger.info("Actualizando dron");
        DroneEntity updatedDrone = droneService.updateDrone(droneDTO, id);
        return new ResponseEntity<>(updatedDrone, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DroneEntity> deleteDrone(@PathVariable Long id) {
        logger.info("Borrando dron");
        DroneEntity deletedDrone = droneService.deleteDroneEntityById(id);
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