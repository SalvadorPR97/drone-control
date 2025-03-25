package com.salvador.droneControl.infrastructure.controller;

import com.salvador.droneControl.application.dto.MatrixDTO;
import com.salvador.droneControl.application.dto.NewMatrixDTO;
import com.salvador.droneControl.application.mapper.MatrixMapper;
import com.salvador.droneControl.application.service.MatrixService;
import com.salvador.droneControl.infrastructure.exception.ResourceNotFoundException;
import com.salvador.droneControl.infrastructure.persistence.entity.MatrixEntity;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/matrix")
public class MatrixController {

    private static final Logger logger = LoggerFactory.getLogger(MatrixController.class);
    private final MatrixService matrixService;
    private final MatrixMapper matrixMapper;

    @Autowired
    public MatrixController(MatrixService matrixService, MatrixMapper matrixMapper) {
        this.matrixService = matrixService;
        this.matrixMapper = matrixMapper;
    }

    @GetMapping("/get/{id}")
    public ResponseEntity<Object> getMatrixById(@PathVariable long id) {
        logger.info("Obteniendo matriz de id: {}", id);
        Optional<MatrixEntity> matrixEntity = matrixService.getMatrixEntityById((int) id);
        if (matrixEntity.isPresent()) {
            return new ResponseEntity<>(matrixEntity.get(), HttpStatus.OK);
        } else {
            logger.error("Matriz no encontrada con id: {}", id);
            throw new ResourceNotFoundException("Matriz no encontrada con id: " + id);
        }
    }

    @PostMapping("/new")
    public ResponseEntity<MatrixDTO> newMatrix(@RequestBody @Valid NewMatrixDTO newMatrixDTO) {
        logger.info("Creando matriz}");
        MatrixDTO matrix = new MatrixDTO();
        matrix.setMax_x(newMatrixDTO.getMax_x());
        matrix.setMax_y(newMatrixDTO.getMax_y());
        MatrixEntity insertedMatrix = matrixService.saveMatrixEntity(matrixMapper.mapToMatrixEntity(matrix));
        matrix.setId(insertedMatrix.getId());
        return new ResponseEntity<>(matrix, HttpStatus.CREATED);
    }

    // TODO evitar que se pueda modificar si tiene algún dron fuera del rango nuevo
    @PatchMapping("/update")
    public ResponseEntity<MatrixDTO> updateMatrix(@RequestBody @Valid MatrixDTO matrixDTO) {
        logger.info("Actualizando matriz");
        Optional<MatrixEntity> oldMatrixEntity = matrixService.getMatrixEntityById(matrixDTO.getId());
        if (oldMatrixEntity.isPresent()) {
            logger.info("Matriz encontrada");
            MatrixEntity updatingMatrixEntity = oldMatrixEntity.get();
            updatingMatrixEntity.setMax_x(matrixDTO.getMax_x());
            updatingMatrixEntity.setMax_y(matrixDTO.getMax_y());
            MatrixEntity updatedMatrixEntity = matrixService.saveMatrixEntity(updatingMatrixEntity);
            matrixDTO.setId(updatedMatrixEntity.getId());
            return new ResponseEntity<>(matrixDTO, HttpStatus.OK);
        } else {
            logger.error("Matriz no encontrada con id: {}", matrixDTO.getId());
            throw new ResourceNotFoundException("Matriz no encontrada con id: " + matrixDTO.getId());
        }
    }
}
