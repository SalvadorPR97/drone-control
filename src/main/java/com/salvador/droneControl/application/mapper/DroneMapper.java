package com.salvador.droneControl.application.mapper;

import com.salvador.droneControl.application.dto.DroneDTO;
import com.salvador.droneControl.application.dto.DroneEntradaDTO;
import com.salvador.droneControl.domain.model.Drone;
import com.salvador.droneControl.domain.model.Orientacion;
import com.salvador.droneControl.infrastructure.persistence.entity.DroneEntity;
import com.salvador.droneControl.infrastructure.persistence.entity.MatrixEntity;
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

    public DroneDTO mapDroneEntityToDTO(DroneEntity droneEntity) {
        DroneDTO droneDTO = modelMapper.map(droneEntity, DroneDTO.class);
        droneDTO.setMatrizId(droneEntity.getMatriz().getId());
        return droneDTO;
    }

    public Drone mapDroneEntityToDrone(DroneEntity droneEntity) {
        Drone drone = modelMapper.map(droneEntity, Drone.class);
        drone.setMatriz_id(droneEntity.getMatriz().getId());
        return drone;
    }

    public DroneEntity mapDroneDTOToEntity(DroneDTO droneDTO) {
        return modelMapper.map(droneDTO, DroneEntity.class);
    }

    public DroneEntity mapEntradaDTOToEntity(DroneEntradaDTO droneEntradaDTO) {
        return modelMapper.map(droneEntradaDTO, DroneEntity.class);
    }

    public DroneEntity mapUpdateDroneDTOToEntity(DroneDTO droneDTO, DroneEntity droneEntity, MatrixEntity matrixEntity) {
        droneEntity.setNombre(droneDTO.getNombre());
        droneEntity.setModelo(droneDTO.getModelo());
        droneEntity.setX(droneDTO.getX());
        droneEntity.setY(droneDTO.getY());
        droneEntity.setOrientacion(Orientacion.valueOf(droneDTO.getOrientacion()));
        droneEntity.setMatriz(matrixEntity);
        return droneEntity;
    }
}
