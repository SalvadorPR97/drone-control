package com.salvador.droneControl.infrastructure.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = MatrixExistsValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface MatrixExists {
    String message() default "La matriz especificada no existe.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
