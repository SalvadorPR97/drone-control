package com.salvador.droneControl.infrastructure.controller;

import com.salvador.droneControl.application.dto.MatrixDTO;
import com.salvador.droneControl.application.dto.MatrixEntradaDTO;
import com.salvador.droneControl.domain.model.Matrix;
import com.salvador.droneControl.domain.service.MatrixService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matrix")
@Tag(name = "Matrix Controller", description = "API para la gestión de matrices")
public class MatrixController {

    private static final Logger logger = LoggerFactory.getLogger(MatrixController.class);
    private final MatrixService matrixService;

    @Autowired
    public MatrixController(MatrixService matrixService) {
        this.matrixService = matrixService;
    }

    @Operation(summary = "Obtener matriz por ID", description = "Recupera una matriz existente por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Matriz encontrada correctamente"),
            @ApiResponse(responseCode = "404", description = "Matriz no encontrada")
    })
    @GetMapping("/get/{id}")
    public ResponseEntity<Matrix> getMatrixById(@Parameter(description = "ID de la matriz a buscar", example = "1") @PathVariable long id) {
        logger.info("Obteniendo matriz de id: {}", id);
        Matrix matrix = matrixService.getMatrixById((int) id);
        return new ResponseEntity<>(matrix, HttpStatus.OK);
    }

    @Operation(summary = "Crear una nueva matriz", description = "Crea una nueva matriz con los datos proporcionados en el DTO.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Matriz creada correctamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos")
    })
    @PostMapping("/new")
    public ResponseEntity<MatrixDTO> newMatrix(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos de la nueva matriz")
                                               @RequestBody @Valid MatrixEntradaDTO matrixEntradaDTO) {
        logger.info("Creando matriz");
        MatrixDTO newMatrix = matrixService.createMatrix(matrixEntradaDTO);
        return new ResponseEntity<>(newMatrix, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar una matriz", description = "Actualiza los datos de una matriz existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Matriz actualizada correctamente"),
            @ApiResponse(responseCode = "404", description = "Matriz no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Matriz no encontrada\"}")))
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<Matrix> updateMatrix(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos para actualizar la matriz")
                                               @RequestBody @Valid MatrixEntradaDTO matrixEntradaDTO,
                                               @Parameter(description = "ID de la matriz a actualizar", example = "1")
                                               @PathVariable long id) {
        logger.info("Actualizando matriz");
        Matrix updatedMatrix = matrixService.updateMatrix(matrixEntradaDTO, id);
        return new ResponseEntity<>(updatedMatrix, HttpStatus.OK);
    }

    @Operation(summary = "Eliminar una matriz", description = "Elimina una matriz por su ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Matriz eliminada correctamente"),
            @ApiResponse(responseCode = "404", description = "Matriz no encontrada",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Matriz no encontrada\"}"))),
            @ApiResponse(responseCode = "409", description = "No se puede eliminar porque tiene drones asociados",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"No se puede eliminar, tiene drones asociados\"}")))
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MatrixDTO> deleteMatrix(@Parameter(description = "ID de la matriz a actualizar", example = "1") @PathVariable Long id) {
        logger.info("Borrando matriz...");
        MatrixDTO deletedMatrix = matrixService.deleteMatrixById(id);
        return new ResponseEntity<>(deletedMatrix, HttpStatus.OK);
    }
}
