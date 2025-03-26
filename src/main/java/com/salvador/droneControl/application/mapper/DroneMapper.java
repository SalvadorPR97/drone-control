package com.salvador.droneControl.application.mapper;

import com.salvador.droneControl.application.dto.DroneDTO;
import com.salvador.droneControl.application.dto.DroneEntradaDTO;
import com.salvador.droneControl.application.dto.DroneNoIdDTO;
import com.salvador.droneControl.domain.model.Drone;
import com.salvador.droneControl.domain.model.Matrix;
import com.salvador.droneControl.domain.model.Orientacion;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DroneMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public DroneMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public DroneNoIdDTO mapDroneEntityToDTO(Drone drone) {
        DroneNoIdDTO droneNoIdDTO = modelMapper.map(drone, DroneNoIdDTO.class);
        droneNoIdDTO.setMatrizId(drone.getMatriz().getId());
        return droneNoIdDTO;
    }

    public DroneDTO mapDroneEntityToDrone(Drone drone) {
        DroneDTO droneDTO = modelMapper.map(drone, DroneDTO.class);
        droneDTO.setMatrizId(drone.getMatriz().getId());
        return droneDTO;
    }

    public Drone mapDroneDTOToEntity(DroneNoIdDTO droneNoIdDTO) {
        return modelMapper.map(droneNoIdDTO, Drone.class);
    }

    public Drone mapEntradaDTOToEntity(DroneEntradaDTO droneEntradaDTO) {
        return modelMapper.map(droneEntradaDTO, Drone.class);
    }

    public Drone mapUpdateDroneDTOToEntity(DroneNoIdDTO droneNoIdDTO, Drone drone, Matrix matrix) {
        drone.setNombre(droneNoIdDTO.getNombre());
        drone.setModelo(droneNoIdDTO.getModelo());
        drone.setX(droneNoIdDTO.getX());
        drone.setY(droneNoIdDTO.getY());
        drone.setOrientacion(Orientacion.valueOf(droneNoIdDTO.getOrientacion()));
        drone.setMatriz(matrix);
        return drone;
    }
}
