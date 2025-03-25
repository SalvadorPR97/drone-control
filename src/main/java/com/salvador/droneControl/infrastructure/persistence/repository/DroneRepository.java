package com.salvador.droneControl.infrastructure.persistence.repository;

import com.salvador.droneControl.infrastructure.persistence.entity.DroneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DroneRepository extends JpaRepository<DroneEntity, Long> {
    @Query("SELECT d FROM DroneEntity d WHERE d.matriz.id = :matrizId AND d.x = :x AND d.y = :y")
    Optional<DroneEntity> findByCoordinates(@Param("matrizId") long matrizId, @Param("x") int x, @Param("y") int y);
}
