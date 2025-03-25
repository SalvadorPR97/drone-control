package com.salvador.droneControl.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class WrongCoordinatesException extends RuntimeException {
    public WrongCoordinatesException(String message) {
        super(message);
    }

    public WrongCoordinatesException(String message, Throwable cause) {
        super(message, cause);
    }
}
