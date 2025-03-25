package com.salvador.droneControl.application.service;

import com.salvador.droneControl.infrastructure.exception.ResourceNotFoundException;
import com.salvador.droneControl.infrastructure.persistence.entity.MatrixEntity;
import com.salvador.droneControl.infrastructure.persistence.repository.MatrixRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatrixService {

    private final MatrixRepository matrixRepository;

    @Autowired
    public MatrixService(MatrixRepository matrixRepository) {
        this.matrixRepository = matrixRepository;
    }

    public MatrixEntity getMatrixEntityById(long id) {
        return matrixRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Matriz no encontrada con id: " + id));
    }

    public MatrixEntity saveMatrixEntity(MatrixEntity matrixEntity) {
        return matrixRepository.save(matrixEntity);
    }

}
