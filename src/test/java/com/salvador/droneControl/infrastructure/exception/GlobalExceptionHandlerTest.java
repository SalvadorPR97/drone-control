package com.salvador.droneControl.infrastructure.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class GlobalExceptionHandlerTest {

    // Supongamos que ERROR_KEY es "message". Si se declara como static final String ERROR_KEY = "message" en la clase, lo usamos.
    private static final String ERROR_KEY = "message";
    private GlobalExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void handleGenericException_returnsInternalServerErrorResponse() {
        // Arrange: Creamos una excepción genérica con un mensaje de prueba
        String errorMsg = "Test generic error";
        Exception ex = new Exception(errorMsg);

        // Act: Llamamos al handler para la excepción genérica
        ResponseEntity<Map<String, String>> responseEntity = exceptionHandler.handleGenericException(ex);

        // Assert: Verificamos que el status sea 500 y el cuerpo contenga el mensaje esperado
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode(), "El status debe ser 500 INTERNAL_SERVER_ERROR");

        Map<String, String> body = responseEntity.getBody();
        assertNotNull(body, "El cuerpo de la respuesta no debe ser null");
        // Verificamos que el mensaje concatenado sea el esperado
        assertThat(body.get(ERROR_KEY), is("Error interno del servidor: " + errorMsg));
    }

    @Test
    void handleJsonParseError_returnsBadRequestResponse() {
        // Act: Llamamos al método que maneja errores de parseo JSON
        ResponseEntity<Map<String, String>> responseEntity = exceptionHandler.handleJsonParseError();

        // Assert: Verificamos que el status sea 400 y el cuerpo contenga el mensaje esperado
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode(), "El status debe ser 400 BAD_REQUEST");

        Map<String, String> body = responseEntity.getBody();
        assertNotNull(body, "El cuerpo de la respuesta no debe ser null");
        String expectedMessage = "Error de formato. Verifique que los valores enviados sean correctos y no estén vacíos.";
        assertThat(body.get(ERROR_KEY), is(expectedMessage));
    }

    @Test
    void handleIllegalArgumentException_returnsBadRequestResponse() {
        // Arrange: Creamos una IllegalArgumentException con un mensaje de prueba.
        String errorMsg = "Test exception";
        IllegalArgumentException ex = new IllegalArgumentException(errorMsg);

        // Act: Llamamos al método del handler
        ResponseEntity<Map<String, String>> responseEntity = exceptionHandler.handleIllegalArgumentException(ex);

        // Assert: Verificamos el status y el contenido del cuerpo de la respuesta
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode(), "El status debe ser 400 BAD REQUEST");

        Map<String, String> body = responseEntity.getBody();
        assertNotNull(body, "El cuerpo de la respuesta no debe ser null");
        assertThat("El mensaje de error debe estar presente en el cuerpo",
                body.get(ERROR_KEY), is("Valor no válido: " + errorMsg));
    }
}