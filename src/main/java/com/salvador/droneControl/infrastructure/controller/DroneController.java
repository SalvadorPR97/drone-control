package com.salvador.droneControl.infrastructure.controller;

import com.salvador.droneControl.application.dto.*;
import com.salvador.droneControl.domain.model.Matrix;
import com.salvador.droneControl.domain.service.DroneService;
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
@RequestMapping("/drone")
@Tag(name = "Drone Controller", description = "API para la gestión de drones")
public class DroneController {

    private static final Logger logger = LoggerFactory.getLogger(DroneController.class);
    private final DroneService droneService;

    @Autowired
    public DroneController(DroneService droneService) {
        this.droneService = droneService;
    }

    @Operation(summary = "Crear un nuevo dron")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Dron creado correctamente"),
            @ApiResponse(responseCode = "400", description = "Solicitud inválida",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Error en los datos de la petición\"}")))
    })
    @PostMapping("/new")
    public ResponseEntity<DroneDTO> newDrone(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos para crear un nuevo dron")
                                             @RequestBody @Valid DroneNoIdDTO droneNoIdDTO) {
        logger.info("Creando dron");
        DroneDTO newDroneDTO = droneService.createDrone(droneNoIdDTO);
        return new ResponseEntity<>(newDroneDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un dron existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dron actualizado correctamente"),
            @ApiResponse(responseCode = "404", description = "Dron no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Dron no encontrado\"}")))
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<DroneDTO> updateDrone(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos para actualizar el dron")
                                                @RequestBody @Valid DroneNoIdDTO droneNoIdDTO,
                                                @Parameter(description = "ID de la matriz a actualizar", example = "1")
                                                @PathVariable Long id) {
        logger.info("Actualizando dron");
        DroneDTO updatedDroneDTO = droneService.updateDrone(droneNoIdDTO, id);
        return new ResponseEntity<>(updatedDroneDTO, HttpStatus.OK);
    }

    @Operation(summary = "Eliminar un dron por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dron eliminado correctamente"),
            @ApiResponse(responseCode = "404", description = "Dron no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Dron no encontrado\"}")))
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<DroneDTO> deleteDrone(@Parameter(description = "ID de la matriz a borrar", example = "1") @PathVariable Long id) {
        logger.info("Borrando dron");
        DroneDTO deletedDroneDTO = droneService.deleteDroneById(id);
        return new ResponseEntity<>(deletedDroneDTO, HttpStatus.OK);
    }

    @Operation(summary = "Buscar un dron por coordenadas")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dron encontrado"),
            @ApiResponse(responseCode = "404", description = "Dron no encontrado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(example = "{\"message\": \"Dron no encontrado en la matriz en las " +
                                    "coordenadas x= x, y= y\"}")))
    })
    @PostMapping("/findByCoordinates")
    public ResponseEntity<DroneDTO> findByCoordinates(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos para buscar un dron por sus coordenadas")
                                                      @RequestBody @Valid DroneCoordinatesDTO droneCoordinatesDTO) {
        logger.info("Buscando dron por coordenadas x={} y={}", droneCoordinatesDTO.getX(), droneCoordinatesDTO.getY());
        DroneDTO droneDTO = droneService.getDroneByCoordinates(
                droneCoordinatesDTO.getMatriz_id(), droneCoordinatesDTO.getX(), droneCoordinatesDTO.getY());
        return new ResponseEntity<>(droneDTO, HttpStatus.OK);
    }

    @Operation(summary = "Mover un dron")
    @ApiResponse(responseCode = "200", description = "Dron movido correctamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = "{\"message\": \"Error encontrado\"}")))
    @PostMapping("/move")
    public ResponseEntity<Matrix> moveOne(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos para mover un dron")
                                          @RequestBody @Valid DroneMoveDTO droneMoveDTO) {
        logger.info("Moviendo dron con id: {}", droneMoveDTO.getId());
        Matrix matrizResultante = droneService.moveOneDrone(droneMoveDTO);
        return new ResponseEntity<>(matrizResultante, HttpStatus.OK);
    }

    @Operation(summary = "Mover varios drones en una matriz")
    @ApiResponse(responseCode = "200", description = "Drones movidos correctamente",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(example = "{\"message\": \"Error encontrado\"}")))
    @PostMapping("/moveManyInMatrix/{id}")
    public ResponseEntity<Matrix> moveMany(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Datos para mover varios drones en un matriz")
                                           @RequestBody @Valid DatosEntradaDTO datosEntradaDTO,
                                           @Parameter(description = "ID de la matriz donde están los drones a mover", example = "1")
                                           @PathVariable Long id) {
        logger.info("Moviendo varios drones");
        Matrix matrix = droneService.moveManyInMatrix(datosEntradaDTO, id);
        return new ResponseEntity<>(matrix, HttpStatus.OK);
    }
}
