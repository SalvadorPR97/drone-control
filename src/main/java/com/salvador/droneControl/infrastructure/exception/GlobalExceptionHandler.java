package com.salvador.droneControl.infrastructure.exception;

import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final String ERROR_KEY = "message";

    @ExceptionHandler(Exception.class)
    @ApiResponse(responseCode = "500", description = "Error interno del servidor",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = "{\"message\": \"Error del servidor\"}")))
    public ResponseEntity<Map<String, String>> handleGenericException(Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put(ERROR_KEY, "Error interno del servidor: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ApiResponse(responseCode = "400", description = "Error de formato en la solicitud",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = "{\"message\": \"Error de formato en la solicitud\"}")))
    public ResponseEntity<Map<String, String>> handleJsonParseError() {
        Map<String, String> response = new HashMap<>();
        response.put(ERROR_KEY, "Error de formato. Verifique que los valores enviados sean correctos y no estén vacíos.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ApiResponse(responseCode = "400", description = "Argumento no válido",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = "{\"message\": \"Argumento no válido\"}")))
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(ERROR_KEY, "Valor no válido: " + ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ApiResponse(responseCode = "404", description = "Recurso no encontrado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = "{\"message\": \"Recurso no encontrado\"}")))
    public ResponseEntity<Map<String, String>> handleResourceNotFound(ResourceNotFoundException ex) {
        System.out.println("-----------------");
        Map<String, String> response = new HashMap<>();
        response.put(ERROR_KEY, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(WrongCoordinatesException.class)
    @ApiResponse(responseCode = "409", description = "Coordenadas incorrectas",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = "{\"message\": \"Coordenadas incorrectas\"}")))
    public ResponseEntity<Map<String, String>> handleWrongCoordinates(WrongCoordinatesException ex) {
        Map<String, String> response = new HashMap<>();
        response.put(ERROR_KEY, ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ApiResponse(responseCode = "400", description = "Error de validación en los datos",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = "{\"message\": \"Error en la validación\"}")))
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(ERROR_KEY, error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ApiResponse(responseCode = "400", description = "Violación de integridad de datos",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = "{\"message\": \"Violación de integridad de datos\"}")))
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolation() {
        Map<String, String> response = new HashMap<>();
        response.put(ERROR_KEY, "No se puede eliminar la matriz porque tiene drones asociados.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
