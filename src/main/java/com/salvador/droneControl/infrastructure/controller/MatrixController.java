package com.salvador.droneControl.infrastructure.controller;

import com.salvador.droneControl.application.dto.NewMatrixDTO;
import com.salvador.droneControl.application.mapper.DroneMapper;
import com.salvador.droneControl.application.mapper.MatrixMapper;
import com.salvador.droneControl.application.service.DroneService;
import com.salvador.droneControl.application.service.MatrixService;
import com.salvador.droneControl.domain.model.Drone;
import com.salvador.droneControl.domain.model.Matrix;
import com.salvador.droneControl.infrastructure.persistence.entity.DroneEntity;
import com.salvador.droneControl.infrastructure.persistence.entity.MatrixEntity;
import com.salvador.droneControl.infrastructure.persistence.repository.MatrixRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/matrix")
public class MatrixController {

    private final MatrixRepository matrixRepository;
    private final MatrixService matrixService;
    private final MatrixMapper matrixMapper;

    @Autowired
    public MatrixController(MatrixRepository matrixRepository, MatrixService matrixService, MatrixMapper matrixMapper) {
        this.matrixRepository = matrixRepository;
        this.matrixService = matrixService;
        this.matrixMapper = matrixMapper;
    }

    @GetMapping("/getMatrix/{id}")
    public ResponseEntity<Matrix> getMatrixById(@PathVariable long id) {
        Optional<MatrixEntity> matrixEntity = matrixService.getMatrixEntityById((int)id);
        if (matrixEntity.isPresent()) {
            Matrix matrix = matrixMapper.mapToMatrix(matrixEntity.get());
            return new ResponseEntity<>(matrix, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/newMatrix")
    public ResponseEntity<NewMatrixDTO> newMatrix(@RequestBody @Valid NewMatrixDTO newMatrixDTO) {
        Matrix matrix = new Matrix();
        matrix.setMax_x(newMatrixDTO.getMax_x());
        matrix.setMax_y(newMatrixDTO.getMax_y());
        MatrixEntity matrixEntity = matrixMapper.mapToMatrixEntity(matrix);
        MatrixEntity insertedMatrix = matrixRepository.save(matrixEntity);
        newMatrixDTO.setId(insertedMatrix.getId());
        return new ResponseEntity<>(newMatrixDTO, HttpStatus.CREATED);
    }

    @PatchMapping("/update/{id}")
    public ResponseEntity<NewMatrixDTO> updateMatrix(@PathVariable long id, @RequestBody @Valid NewMatrixDTO newMatrixDTO) {
        Optional<MatrixEntity> oldMatrixEntity = matrixService.getMatrixEntityById(id);
        if (oldMatrixEntity.isPresent()) {
            MatrixEntity updatingMatrixEntity = oldMatrixEntity.get();
            updatingMatrixEntity.setMax_x(newMatrixDTO.getMax_x());
            updatingMatrixEntity.setMax_y(newMatrixDTO.getMax_y());
            MatrixEntity updatedMatrixEntity = matrixRepository.save(updatingMatrixEntity);
            newMatrixDTO.setId(updatedMatrixEntity.getId());
            return new ResponseEntity<>(newMatrixDTO, HttpStatus.OK);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
