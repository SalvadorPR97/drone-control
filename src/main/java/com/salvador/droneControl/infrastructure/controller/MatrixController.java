package com.salvador.droneControl.infrastructure.controller;

import com.salvador.droneControl.application.dto.MatrixDTO;
import com.salvador.droneControl.application.mapper.MatrixMapper;
import com.salvador.droneControl.application.service.MatrixService;
import com.salvador.droneControl.domain.model.Matrix;
import com.salvador.droneControl.infrastructure.persistence.entity.MatrixEntity;
import com.salvador.droneControl.infrastructure.persistence.repository.MatrixRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/matrix")
public class MatrixController {

    private final MatrixService matrixService;
    private final MatrixMapper matrixMapper;

    @Autowired
    public MatrixController(MatrixRepository matrixRepository, MatrixService matrixService, MatrixMapper matrixMapper) {
        this.matrixService = matrixService;
        this.matrixMapper = matrixMapper;
    }

    @GetMapping("/getMatrix/{id}")
    public ResponseEntity<?> getMatrixById(@PathVariable long id) {
        Optional<MatrixEntity> matrixEntity = matrixService.getMatrixEntityById((int) id);
        if (matrixEntity.isPresent()) {

            return new ResponseEntity<>(matrixEntity.get(), HttpStatus.OK);
        } else {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Matriz no encontrada");
            errorResponse.put("message", "No se encontró ninguna matriz con el ID " + id);

            return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/newMatrix")
    public ResponseEntity<MatrixDTO> newMatrix(@RequestBody @Valid MatrixDTO matrixDTO) {
        Matrix matrix = new Matrix();
        matrix.setMax_x(matrixDTO.getMax_x());
        matrix.setMax_y(matrixDTO.getMax_y());
        MatrixEntity matrixEntity = matrixMapper.mapToMatrixEntity(matrix);
        MatrixEntity insertedMatrix = matrixService.saveMatrixEntity(matrixEntity);
        matrixDTO.setId(insertedMatrix.getId());
        return new ResponseEntity<>(matrixDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<MatrixDTO> updateMatrix(@PathVariable long id, @RequestBody @Valid MatrixDTO matrixDTO) {
        Optional<MatrixEntity> oldMatrixEntity = matrixService.getMatrixEntityById(id);
        if (oldMatrixEntity.isPresent()) {
            MatrixEntity updatingMatrixEntity = oldMatrixEntity.get();
            updatingMatrixEntity.setMax_x(matrixDTO.getMax_x());
            updatingMatrixEntity.setMax_y(matrixDTO.getMax_y());
            MatrixEntity updatedMatrixEntity = matrixService.saveMatrixEntity(updatingMatrixEntity);
            matrixDTO.setId(updatedMatrixEntity.getId());
            return new ResponseEntity<>(matrixDTO, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
