package com.salvador.droneControl.application.mapper;

import com.salvador.droneControl.application.dto.DroneDTO;
import com.salvador.droneControl.application.dto.DroneEntradaDTO;
import com.salvador.droneControl.application.dto.DroneNoIdDTO;
import com.salvador.droneControl.domain.model.Drone;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class DroneMapper {

    private final ModelMapper modelMapper;

    public DroneMapper() {
        this.modelMapper = new ModelMapper();
    }

    public DroneDTO mapDroneToDroneDTO(Drone drone) {
        DroneDTO droneDTO = modelMapper.map(drone, DroneDTO.class);
        droneDTO.setMatrizId(drone.getMatriz().getId());
        return droneDTO;
    }

    public Drone mapDroneNoIdDTOToDrone(DroneNoIdDTO droneNoIdDTO) {
        return modelMapper.map(droneNoIdDTO, Drone.class);
    }

    public Drone mapDroneEntradaDTOToDrone(DroneEntradaDTO droneEntradaDTO) {
        return modelMapper.map(droneEntradaDTO, Drone.class);
    }
}
