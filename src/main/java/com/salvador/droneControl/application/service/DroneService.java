package com.salvador.droneControl.application.service;

import com.salvador.droneControl.infrastructure.exception.ResourceNotFoundException;
import com.salvador.droneControl.infrastructure.persistence.entity.DroneEntity;
import com.salvador.droneControl.infrastructure.persistence.repository.DroneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DroneService {

    private final DroneRepository droneRepository;

    @Autowired
    public DroneService(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    public DroneEntity getDroneEntityById(long id) {
        return droneRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Drone no encontrado con id: " + id));
    }

    public DroneEntity saveDroneEntity(DroneEntity droneEntity) {
        return droneRepository.save(droneEntity);
    }

    public DroneEntity deleteDroneEntity(DroneEntity droneEntity) {
        droneRepository.delete(droneEntity);
        return droneEntity;
    }

    public DroneEntity getDroneEntityByCoordinates(long matrizId, int x, int y) {

        return droneRepository.findByCoordinates(matrizId, x, y).orElseThrow(() ->
                new ResourceNotFoundException("Drone no encontrado en la matriz en las coordenadas x= " +
                        x + ", y= " + y));
    }

}
