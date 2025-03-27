package com.salvador.droneControl.domain.repository;

import com.salvador.droneControl.domain.model.Drone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DroneRepository extends JpaRepository<Drone, Long> {
    @Query("SELECT d FROM Drone d WHERE d.matriz.id = :matrizId AND d.x = :x AND d.y = :y")
    Optional<Drone> findByCoordinates(@Param("matrizId") long matrizId, @Param("x") int x, @Param("y") int y);
}
