package com.salvador.droneControl.infrastructure.controller;

import com.salvador.droneControl.application.dto.DroneDTO;
import com.salvador.droneControl.application.mapper.DroneMapper;
import com.salvador.droneControl.application.service.DroneService;
import com.salvador.droneControl.application.service.MatrixService;
import com.salvador.droneControl.domain.model.Orientacion;
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

    @PostMapping("/new")
    public ResponseEntity<?> newDrone(@RequestBody @Valid DroneDTO droneDTO) {
        DroneEntity droneEntity = droneMapper.mapToEntity(droneDTO);
        Optional<MatrixEntity> optMatrixEntity = matrixService.getMatrixEntityById(droneDTO.getMatriz_id());
        if (optMatrixEntity.isPresent()) {
            MatrixEntity matrixEntity = optMatrixEntity.get();
            ResponseEntity<?> checkCoordinatesOutOfMatrix = coordinatesOutOfMatrix(droneDTO, matrixEntity);
            if (checkCoordinatesOutOfMatrix != null) {
                return checkCoordinatesOutOfMatrix;
            }
            ResponseEntity<?> checkCoordinatesBusy = coordinatesBusy(droneDTO, matrixEntity);
            if (checkCoordinatesBusy != null) {
                return checkCoordinatesBusy;
            }

            droneEntity.setMatriz(matrixEntity);
            DroneEntity createdDroneEntity = droneService.saveDroneEntity(droneEntity);
            droneDTO.setId(createdDroneEntity.getId());
            return new ResponseEntity<>(droneDTO, HttpStatus.CREATED);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<?> updateDrone(@RequestBody @Valid DroneDTO droneDTO, @PathVariable Long id) {
        Optional<DroneEntity> optOldDroneEntity = droneService.getDroneById(id);
        Optional<MatrixEntity> optMatrixEntity = matrixService.getMatrixEntityById(droneDTO.getMatriz_id());
        if (optOldDroneEntity.isPresent() && optMatrixEntity.isPresent()) {
            MatrixEntity matrixEntity = optMatrixEntity.get();
            ResponseEntity<?> checkCoordinatesOutOfMatrix = coordinatesOutOfMatrix(droneDTO, matrixEntity);
            if (checkCoordinatesOutOfMatrix != null) {
                return checkCoordinatesOutOfMatrix;
            }

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
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDrone(@PathVariable Long id) {
        Optional<DroneEntity> optDroneEntity = droneService.getDroneById(id);
        if (optDroneEntity.isPresent()) {
            DroneEntity droneEntity = optDroneEntity.get();
            DroneEntity deletedDrone = droneService.deleteDroneEntity(droneEntity);
            return new ResponseEntity<>(deletedDrone, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    private ResponseEntity<?> coordinatesOutOfMatrix(DroneDTO droneDTO, MatrixEntity matrixEntity) {
        if (droneDTO.getX() > matrixEntity.getMax_x() || droneDTO.getY() > matrixEntity.getMax_y()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Coordenada no encontrada");
            errorResponse.put("message", "La coordenada excede el límite de la matriz");
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }
        return null;
    }

    private ResponseEntity<?> coordinatesBusy(DroneDTO droneDTO, MatrixEntity matrixEntity) {
        List<DroneEntity> drones = matrixEntity.getDrones();
        for (DroneEntity drone : drones) {
            if (drone.getX() == droneDTO.getX() && drone.getY() == droneDTO.getY()) {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Coordenada ocupada");
                errorResponse.put("message", "Coordenadas ocupadas por el dron con el ID " + drone.getId());
                return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
            }
        }
        return null;
    }

    /*private ResponseEntity<?> checkCoordinates(DroneDTO droneDTO, MatrixEntity matrixEntity, DroneEntity droneEntity) {
        if (droneDTO.getX() > matrixEntity.getMax_x() || droneDTO.getY() > matrixEntity.getMax_y()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Coordenada no encontrada");
            errorResponse.put("message", "La coordenada excede el límite de la matriz");
            return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
        }

        if (droneEntity == null) {
            List<DroneEntity> drones = matrixEntity.getDrones();
            for (DroneEntity drone : drones) {
                if (drone.getX() == droneDTO.getX() && drone.getY() == droneDTO.getY()) {
                    Map<String, String> errorResponse = new HashMap<>();
                    errorResponse.put("error", "Coordenada ocupada");
                    errorResponse.put("message", "Coordenadas ocupadas por el dron con el ID " + drone.getId());
                    return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
                }
            }
        }

        return null;
    }*/

}
