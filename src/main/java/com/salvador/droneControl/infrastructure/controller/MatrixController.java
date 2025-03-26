package com.salvador.droneControl.infrastructure.controller;

import com.salvador.droneControl.application.dto.MatrixDTO;
import com.salvador.droneControl.application.dto.NewMatrixDTO;
import com.salvador.droneControl.application.service.MatrixService;
import com.salvador.droneControl.infrastructure.persistence.entity.MatrixEntity;
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
    public ResponseEntity<MatrixEntity> getMatrixById(@PathVariable long id) {
        logger.info("Obteniendo matriz de id: {}", id);
        MatrixEntity matrixEntity = matrixService.getMatrixEntityById((int) id);
        return new ResponseEntity<>(matrixEntity, HttpStatus.OK);
    }

    @PostMapping("/new")
    public ResponseEntity<MatrixDTO> newMatrix(@RequestBody @Valid NewMatrixDTO newMatrixDTO) {
        logger.info("Creando matriz}");
        MatrixDTO newMatrix = matrixService.createMatrix(newMatrixDTO);
        return new ResponseEntity<>(newMatrix, HttpStatus.CREATED);
    }

    @PatchMapping("/update")
    public ResponseEntity<MatrixEntity> updateMatrix(@RequestBody @Valid MatrixDTO matrixDTO) {
        logger.info("Actualizando matriz");
        MatrixEntity updatedMatrix = matrixService.updateMatrix(matrixDTO);
        return new ResponseEntity<>(updatedMatrix, HttpStatus.OK);

    }


}
