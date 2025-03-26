package com.salvador.droneControl.infrastructure.controller;

import com.salvador.droneControl.application.dto.*;
import com.salvador.droneControl.domain.model.Matrix;
import com.salvador.droneControl.domain.service.DroneService;
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
    public ResponseEntity<DroneDTO> newDrone(@RequestBody @Valid DroneNoIdDTO droneNoIdDTO) {
        logger.info("Creando dron");
        DroneDTO newDroneDTO = droneService.createDrone(droneNoIdDTO);
        return new ResponseEntity<>(newDroneDTO, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DroneDTO> updateDrone(@RequestBody @Valid DroneNoIdDTO droneNoIdDTO, @PathVariable Long id) {
        logger.info("Actualizando dron");
        DroneDTO updatedDroneDTO = droneService.updateDrone(droneNoIdDTO, id);
        return new ResponseEntity<>(updatedDroneDTO, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DroneDTO> deleteDrone(@PathVariable Long id) {
        logger.info("Borrando dron");
        DroneDTO deletedDroneDTO = droneService.deleteDroneEntityById(id);
        return new ResponseEntity<>(deletedDroneDTO, HttpStatus.OK);
    }

    @PostMapping("/findByCoordinates")
    public ResponseEntity<DroneDTO> findByCoordinates(@RequestBody @Valid DroneCoordinatesDTO droneCoordinatesDTO) {
        logger.info("Buscando dron por coordenadas x={} y={}", droneCoordinatesDTO.getX(), droneCoordinatesDTO.getY());
        DroneDTO droneDTO = droneService.getDroneByCoordinates(
                droneCoordinatesDTO.getMatriz_id(), droneCoordinatesDTO.getX(), droneCoordinatesDTO.getY());
        return new ResponseEntity<>(droneDTO, HttpStatus.OK);
    }

    @PostMapping("/move")
    public ResponseEntity<Matrix> moveOne(@RequestBody @Valid DroneMoveDTO droneMoveDTO) {
        logger.info("Moviendo dron con id: {}", droneMoveDTO.getId());
        Matrix matrizResultante = droneService.moveOneDrone(droneMoveDTO);
        return new ResponseEntity<>(matrizResultante, HttpStatus.OK);
    }

    @PostMapping("/moveManyInMatrix/{id}")
    public ResponseEntity<Matrix> moveMany(@RequestBody @Valid DatosEntradaDTO datosEntradaDTO, @PathVariable Long id) {
        logger.info("Moviendo varios drones");
        Matrix matrix = droneService.moveManyInMatrix(datosEntradaDTO, id);
        return new ResponseEntity<>(matrix, HttpStatus.OK);
    }

}