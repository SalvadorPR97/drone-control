package com.salvador.droneControl.infrastructure.validation;

import com.salvador.droneControl.domain.repository.DroneRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

public class DroneExistsValidator implements ConstraintValidator<DroneExists, Long> {

    private final DroneRepository droneRepository;

    @Autowired
    public DroneExistsValidator(DroneRepository droneRepository) {
        this.droneRepository = droneRepository;
    }

    @Override
    public boolean isValid(Long droneId, ConstraintValidatorContext context) {
        if (droneId == null) {
            return false;
        }
        return droneRepository.existsById(droneId);
    }
}
