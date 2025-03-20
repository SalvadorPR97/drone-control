package com.salvador.droneControl.infrastructure.controller;

import com.salvador.droneControl.application.mapper.DroneMapper;
import com.salvador.droneControl.application.service.DroneService;
import com.salvador.droneControl.domain.model.Drone;
import com.salvador.droneControl.infrastructure.persistence.entity.DroneEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api")
public class DroneController {

    private final DroneService droneService;
    private final DroneMapper droneMapper;

    @Autowired
    public DroneController(DroneService droneService, DroneMapper droneMapper) {
        this.droneService = droneService;
        this.droneMapper = droneMapper;
    }

    @GetMapping("/getDrone/{id}")
    public ResponseEntity<Drone> getDroneById(@PathVariable long id) {
        Optional<DroneEntity> droneEntity = droneService.getDroneById((int)id);
        if (droneEntity.isPresent()) {
            Drone drone = droneMapper.mapToDrone(droneEntity.get());
            return new ResponseEntity<>(drone, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
