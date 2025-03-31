package com.salvador.droneControl.infrastructure.controller;

import com.salvador.droneControl.application.dto.MatrixDTO;
import com.salvador.droneControl.application.dto.MatrixEntradaDTO;
import com.salvador.droneControl.domain.model.Matrix;
import com.salvador.droneControl.domain.service.MatrixService;
import com.salvador.droneControl.infrastructure.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MatrixController.class)
class MatrixControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Usamos @MockBean para que Spring reemplace el bean real por un mock en el contexto
    @MockBean
    private MatrixService matrixService;

    private long matrixId;
    private Matrix matrix;

    @BeforeEach
    void setUp() {
        matrixId = 1L;
        matrix = new Matrix(matrixId, 5, 5, null);
    }

    @Test
    void getMatrixById_found() throws Exception {

        when(matrixService.getMatrixById(matrixId)).thenReturn(matrix);

        mockMvc.perform(get("/matrix/get/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(matrix.getId()))
                .andExpect(jsonPath("$.max_x").value(matrix.getMax_x()))
                .andExpect(jsonPath("$.max_y").value(matrix.getMax_y()));
        verify(matrixService).getMatrixById(matrixId);
    }

    @Test
    void getMatrixById_notFound() throws Exception {


        when(matrixService.getMatrixById(matrixId))
                .thenThrow(new ResourceNotFoundException("Matriz no encontrada con id: " + matrixId));

        mockMvc.perform(get("/matrix/get/{id}", matrixId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").exists());
        verify(matrixService).getMatrixById(matrixId);
    }

    @Test
    void newMatrix_success() throws Exception {
        MatrixEntradaDTO matrixEntradaDTO = new MatrixEntradaDTO(5, 5);
        MatrixDTO matrixDTO = new MatrixDTO(1L, 5, 5);

        when(matrixService.createMatrix(matrixEntradaDTO)).thenReturn(matrixDTO);

        mockMvc.perform(post("/matrix/new")
                        .contentType("application/json")
                        .content("{\"max_x\":5,\"max_y\":5}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(matrixDTO.getId()))
                .andExpect(jsonPath("$.max_x").value(matrixDTO.getMax_x()))
                .andExpect(jsonPath("$.max_y").value(matrixDTO.getMax_y()));
        verify(matrixService).createMatrix(matrixEntradaDTO);
    }

    @Test
    void newMatrix_invalidData() throws Exception {
        mockMvc.perform(post("/matrix/new")
                        .contentType("application/json")
                        .content("{\"max_x\":-1,\"max_y\":-1}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void updateMatrix_success() throws Exception {
        MatrixEntradaDTO entradaDTO = new MatrixEntradaDTO(7, 7);
        com.salvador.droneControl.domain.model.Matrix updatedMatrix =
                new com.salvador.droneControl.domain.model.Matrix(matrixId, 7, 7, null);

        when(matrixService.updateMatrix(entradaDTO, matrixId)).thenReturn(updatedMatrix);

        String jsonContent = "{\"max_x\":7,\"max_y\":7}";

        mockMvc.perform(put("/matrix/update/{id}", matrixId)
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is((int) matrixId)))
                .andExpect(jsonPath("$.max_x", is(7)))
                .andExpect(jsonPath("$.max_y", is(7)));
    }

    @Test
    void updateMatrix_notFound() throws Exception {
        MatrixEntradaDTO entradaDTO = new MatrixEntradaDTO(7, 7);
        String errorMessage = "Matriz no encontrada con id: " + matrixId;
        when(matrixService.updateMatrix(entradaDTO, matrixId))
                .thenThrow(new ResourceNotFoundException(errorMessage));

        String jsonContent = "{\"max_x\":7,\"max_y\":7}";

        mockMvc.perform(put("/matrix/update/{id}", matrixId)
                        .contentType("application/json")
                        .content(jsonContent))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(errorMessage)));
    }

    @Test
    void deleteMatrix_success() throws Exception {
        MatrixDTO deletedMatrixDTO = new MatrixDTO(matrixId, 5, 5);

        when(matrixService.deleteMatrixById(matrixId)).thenReturn(deletedMatrixDTO);

        mockMvc.perform(delete("/matrix/delete/{id}", matrixId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.max_x", is(5)))
                .andExpect(jsonPath("$.max_y", is(5)));
    }

    @Test
    void deleteMatrix_notFound() throws Exception {
        String errorMessage = "Matriz no encontrada con id: " + matrixId;
        when(matrixService.deleteMatrixById(matrixId))
                .thenThrow(new ResourceNotFoundException(errorMessage));

        mockMvc.perform(delete("/matrix/delete/{id}", matrixId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is(errorMessage)));
    }

}
