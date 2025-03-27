package com.salvador.droneControl.infrastructure.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DroneExistsValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface DroneExists {
    String message() default "El dron especificado no existe.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
