package com.salvador.droneControl.application.mapper;

import com.salvador.droneControl.domain.model.Drone;
import com.salvador.droneControl.infrastructure.persistence.entity.DroneEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class DroneMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public DroneMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public Drone mapToDrone(DroneEntity droneEntity) {
        return modelMapper.map(droneEntity, Drone.class);
    }

    public DroneEntity mapToDroneEntity(Drone drone) {
        return modelMapper.map(drone, DroneEntity.class);
    }
}
