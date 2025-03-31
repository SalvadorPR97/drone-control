package com.salvador.droneControl.infrastructure.controller;

import com.salvador.droneControl.domain.service.MatrixService;
import com.salvador.droneControl.infrastructure.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(MatrixController.class)
class MatrixControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Usamos @MockBean para que Spring reemplace el bean real por un mock en el contexto
    @MockBean
    private MatrixService matrixService;

    @Test
    void getMatrixById_notFound() throws Exception {
        long matrixId = 1L;

        // Configuramos el servicio para lanzar la excepción cuando se busque la matriz
        when(matrixService.getMatrixById(matrixId))
                .thenThrow(new ResourceNotFoundException("Matriz no encontrada con id: " + matrixId));

        // Realizamos la petición GET y verificamos el código 404 y el mensaje en el JSON
        mockMvc.perform(get("/matrix/get/{id}", matrixId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Matriz no encontrada con id: " + matrixId)));
    }
}
