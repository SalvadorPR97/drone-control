package com.salvador.droneControl.application.service;

import com.salvador.droneControl.application.dto.DroneCoordinatesDTO;
import com.salvador.droneControl.infrastructure.persistence.entity.DroneEntity;
import com.salvador.droneControl.infrastructure.persistence.repository.DroneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DroneService {

    private final DroneRepository droneRepository;

    @Autowired
    public DroneService(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    public Optional<DroneEntity> getDroneById(long id) {
        return droneRepository.findById(id);
    }

    public DroneEntity saveDroneEntity(DroneEntity droneEntity) {
        return droneRepository.save(droneEntity);
    }

    public DroneEntity deleteDroneEntity(DroneEntity droneEntity) {
        droneRepository.delete(droneEntity);
        return droneEntity;
    }

    public Optional<DroneEntity> getDroneEntityByCoordinates(long matriz_id, int x, int y) {

        return droneRepository.findByCoordinates(matriz_id, x, y);
    }

}
