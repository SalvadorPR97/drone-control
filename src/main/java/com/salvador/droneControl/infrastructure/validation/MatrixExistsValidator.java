package com.salvador.droneControl.infrastructure.validation;

import com.salvador.droneControl.domain.repository.MatrixRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class MatrixExistsValidator implements ConstraintValidator<MatrixExists, Long> {

    private final MatrixRepository matrixRepository;

    @Autowired
    public MatrixExistsValidator(MatrixRepository matrixRepository) {
        this.matrixRepository = matrixRepository;
    }

    @Override
    public boolean isValid(Long matrixId, ConstraintValidatorContext context) {
        if (matrixId == null) {
            return false;
        }
        return matrixRepository.existsById(matrixId);
    }
}
