package com.salvador.droneControl.application.mapper;

import com.salvador.droneControl.application.dto.DroneDTO;
import com.salvador.droneControl.application.dto.DroneEntradaDTO;
import com.salvador.droneControl.application.service.MatrixService;
import com.salvador.droneControl.infrastructure.persistence.entity.DroneEntity;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DroneMapper {

    private final ModelMapper modelMapper;
    private final MatrixService matrixService;

    @Autowired
    public DroneMapper(ModelMapper modelMapper, MatrixService matrixService) {
        this.modelMapper = modelMapper;
        this.matrixService = matrixService;
    }

    public DroneDTO mapToDTO(DroneEntity droneEntity) {
        DroneDTO droneDTO = modelMapper.map(droneEntity, DroneDTO.class);
        if (droneEntity.getMatriz() != null) {
            droneDTO.setMatriz_id(droneEntity.getMatriz().getId());
        }
        return droneDTO;
    }

    // Convertir de DroneDTO a DroneEntity
    public DroneEntity mapToEntity(DroneDTO droneDTO) {
        return modelMapper.map(droneDTO, DroneEntity.class);
    }

    public DroneEntity mapEntradaDTOToEntity(DroneEntradaDTO droneEntradaDTO) {return modelMapper.map(droneEntradaDTO, DroneEntity.class);}
}
