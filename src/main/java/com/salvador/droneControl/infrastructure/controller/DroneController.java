package com.salvador.droneControl.infrastructure.controller;

import com.salvador.droneControl.application.dto.DroneDTO;
import com.salvador.droneControl.application.dto.MatrixDTO;
import com.salvador.droneControl.application.mapper.DroneMapper;
import com.salvador.droneControl.application.service.DroneService;
import com.salvador.droneControl.application.service.MatrixService;
import com.salvador.droneControl.domain.model.Drone;
import com.salvador.droneControl.domain.model.Matrix;
import com.salvador.droneControl.infrastructure.persistence.entity.DroneEntity;
import com.salvador.droneControl.infrastructure.persistence.entity.MatrixEntity;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/drone")
public class DroneController {

    private final DroneService droneService;
    private final DroneMapper droneMapper;
    private final MatrixService matrixService;

    @Autowired
    public DroneController(DroneService droneService, DroneMapper droneMapper, MatrixService matrixService) {
        this.droneService = droneService;
        this.droneMapper = droneMapper;
        this.matrixService = matrixService;
    }

    /*@GetMapping("/getDrone/{id}")
    public ResponseEntity<Drone> getDroneById(@PathVariable long id) {
        Optional<DroneEntity> droneEntity = droneService.getDroneById((int) id);
        if (droneEntity.isPresent()) {
            Drone drone = droneMapper.mapToDrone(droneEntity.get());
            return new ResponseEntity<>(drone, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }*/

    @PostMapping("/newDrone")
    public ResponseEntity<?> newDrone(@RequestBody @Valid DroneDTO droneDTO) {
        DroneEntity droneEntity = droneMapper.mapToEntity(droneDTO);
        Optional<MatrixEntity> optMatrixEntity = matrixService.getMatrixEntityById(droneDTO.getMatriz_id());
        if (optMatrixEntity.isPresent()) {
            MatrixEntity matrixEntity = optMatrixEntity.get();
            if (droneEntity.getX() > matrixEntity.getMax_x() || droneEntity.getY() > matrixEntity.getMax_y()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Coordenada no encontrada");
                errorResponse.put("message", "La coordenada excede el límite de la matriz");
                return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
            }

            List<DroneEntity> drones = matrixEntity.getDrones();
            for (DroneEntity drone : drones) {
                if (drone.getX() == droneDTO.getX() && drone.getY() == droneDTO.getY()) {
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("error", "Coordenada ocupada");
                    errorResponse.put("message", "Coordenadas ocupadas por el dron con el ID " + drone.getId());
                    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
                }

            }

            droneEntity.setMatriz(matrixEntity);
            DroneEntity createdDroneEntity = droneService.saveDroneEntity(droneEntity);

            droneDTO.setId(createdDroneEntity.getId());
            return new ResponseEntity<>(droneDTO, HttpStatus.CREATED);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
