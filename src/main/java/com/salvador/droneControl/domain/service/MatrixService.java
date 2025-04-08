package com.salvador.droneControl.domain.service;

import com.salvador.droneControl.application.dto.MatrixDTO;
import com.salvador.droneControl.application.dto.MatrixEntradaDTO;
import com.salvador.droneControl.application.mapper.MatrixMapper;
import com.salvador.droneControl.domain.model.Drone;
import com.salvador.droneControl.domain.model.Matrix;
import com.salvador.droneControl.domain.repository.MatrixRepository;
import com.salvador.droneControl.infrastructure.exception.ResourceNotFoundException;
import com.salvador.droneControl.infrastructure.exception.WrongCoordinatesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

    public List<Long> getMatricesIds() {
        List<Matrix> matrices = matrixRepository.findAll();
        List<Long> matricesIds = new ArrayList<>();
        for (Matrix matrix : matrices) {
            matricesIds.add(matrix.getId());
        }
        return matricesIds;
    }
    public Matrix getMatrixById(long id) {
        return matrixRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Matriz no encontrada con id: " + id));
    }

    public Matrix saveMatrix(Matrix matrix) {
        return matrixRepository.save(matrix);
    }

    public MatrixDTO createMatrix(MatrixEntradaDTO matrixEntradaDTO) {
        MatrixDTO matrix = new MatrixDTO();
        matrix.setMax_x(matrixEntradaDTO.getMax_x());
        matrix.setMax_y(matrixEntradaDTO.getMax_y());
        Matrix insertedMatrix = this.saveMatrix(matrixMapper.mapMatrixDTOToMatrix(matrix));

        matrix.setId(insertedMatrix.getId());
        return matrix;
    }

    public Matrix updateMatrix(MatrixEntradaDTO matrixEntradaDTO, long id) {
        Matrix oldMatrix = this.getMatrixById(id);
        oldMatrix.setMax_x(matrixEntradaDTO.getMax_x());
        oldMatrix.setMax_y(matrixEntradaDTO.getMax_y());
        for (Drone drone : oldMatrix.getDrones()) {
            this.droneOutOfMatrix(drone, oldMatrix);
        }
        return this.saveMatrix(oldMatrix);
    }

    public MatrixDTO deleteMatrixById(Long id) {
        Matrix matrix = this.getMatrixById(id);
        matrixRepository.deleteById(id);
        return matrixMapper.mapMatrixToMatrixDTO(matrix);
    }

    public void droneOutOfMatrix(Drone drone, Matrix matrix) {
        if (drone.getX() >= matrix.getMax_x() || drone.getY() >= matrix.getMax_y()) {
            String errorMessage = "Quedarían drones fuera de la nueva matriz";
            logger.error(errorMessage);
            throw new WrongCoordinatesException(errorMessage);
        }
    }
}
