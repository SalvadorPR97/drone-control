package com.salvador.droneControl.application.service;

import com.salvador.droneControl.application.dto.MatrixDTO;
import com.salvador.droneControl.application.dto.NewMatrixDTO;
import com.salvador.droneControl.application.mapper.MatrixMapper;
import com.salvador.droneControl.infrastructure.exception.ResourceNotFoundException;
import com.salvador.droneControl.infrastructure.exception.WrongCoordinatesException;
import com.salvador.droneControl.infrastructure.persistence.entity.DroneEntity;
import com.salvador.droneControl.infrastructure.persistence.entity.MatrixEntity;
import com.salvador.droneControl.infrastructure.persistence.repository.MatrixRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatrixService {

    private static final Logger logger = LoggerFactory.getLogger(MatrixService.class);
    private final MatrixRepository matrixRepository;
    private final MatrixMapper matrixMapper;

    @Autowired
    public MatrixService(MatrixRepository matrixRepository, MatrixMapper matrixMapper) {
        this.matrixRepository = matrixRepository;
        this.matrixMapper = matrixMapper;
    }

    public MatrixEntity getMatrixEntityById(long id) {
        return matrixRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Matriz no encontrada con id: " + id));
    }

    public MatrixEntity saveMatrixEntity(MatrixEntity matrixEntity) {
        return matrixRepository.save(matrixEntity);
    }

    public MatrixDTO createMatrix(NewMatrixDTO newMatrixDTO) {
        MatrixDTO matrix = new MatrixDTO();
        matrix.setMax_x(newMatrixDTO.getMax_x());
        matrix.setMax_y(newMatrixDTO.getMax_y());
        MatrixEntity insertedMatrix = this.saveMatrixEntity(matrixMapper.mapToMatrixEntity(matrix));

        matrix.setId(insertedMatrix.getId());
        return matrix;
    }

    public MatrixEntity updateMatrix(MatrixDTO matrixDTO) {
        MatrixEntity oldMatrixEntity = this.getMatrixEntityById(matrixDTO.getId());
        oldMatrixEntity.setMax_x(matrixDTO.getMax_x());
        oldMatrixEntity.setMax_y(matrixDTO.getMax_y());
        for (DroneEntity drone : oldMatrixEntity.getDrones()) {
            this.droneOutOfMatrix(drone, oldMatrixEntity);
        }
        return this.saveMatrixEntity(oldMatrixEntity);
    }

    private void droneOutOfMatrix(DroneEntity drone, MatrixEntity matrixEntity) {
        if (drone.getX() > matrixEntity.getMax_x() || drone.getY() > matrixEntity.getMax_y()) {
            String errorMessage = "Quedarían drones fuera de la nueva matriz";
            logger.error(errorMessage);
            throw new WrongCoordinatesException(errorMessage);
        }
    }
}
