package com.salvador.droneControl.infrastructure.controller;

import com.salvador.droneControl.application.dto.MatrixDataDTO;
import com.salvador.droneControl.application.dto.MatrixEntradaDTO;
import com.salvador.droneControl.domain.model.Matrix;
import com.salvador.droneControl.domain.service.MatrixService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/matrix")
public class MatrixController {

    private static final Logger logger = LoggerFactory.getLogger(MatrixController.class);
    private final MatrixService matrixService;

    @Autowired
    public MatrixController(MatrixService matrixService) {
        this.matrixService = matrixService;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Matrix> getMatrixById(@PathVariable long id) {
        logger.info("Obteniendo matriz de id: {}", id);
        Matrix matrix = matrixService.getMatrixEntityById((int) id);
        return new ResponseEntity<>(matrix, HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<MatrixDataDTO> newMatrix(@RequestBody @Valid MatrixEntradaDTO matrixEntradaDTO) {
        logger.info("Creando matriz}");
        MatrixDataDTO newMatrix = matrixService.createMatrix(matrixEntradaDTO);
        return new ResponseEntity<>(newMatrix, HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Matrix> updateMatrix(@RequestBody @Valid MatrixEntradaDTO matrixEntradaDTO, @PathVariable long id) {
        logger.info("Actualizando matriz");
        Matrix updatedMatrix = matrixService.updateMatrix(matrixEntradaDTO, id);
        return new ResponseEntity<>(updatedMatrix, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<MatrixDataDTO> deleteMatrix(@PathVariable Long id) {
        logger.info("Borrando matriz...");
        MatrixDataDTO deletedMatrix = matrixService.deleteMatrixById(id);
        return new ResponseEntity<>(deletedMatrix, HttpStatus.OK);
    }
}
