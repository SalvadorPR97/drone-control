package com.salvador.droneControl.application.service;

import com.salvador.droneControl.infrastructure.persistence.entity.MatrixEntity;
import com.salvador.droneControl.infrastructure.persistence.repository.MatrixRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MatrixService {

    private final MatrixRepository matrixRepository;

    @Autowired
    public MatrixService(MatrixRepository matrixRepository) {
        this.matrixRepository = matrixRepository;
    }

    public Optional<MatrixEntity> getMatrixEntityById(long id) {
        return matrixRepository.findById(id);
    }

    public MatrixEntity saveMatrixEntity(MatrixEntity matrixEntity) {
        return matrixRepository.save(matrixEntity);
    }

}
