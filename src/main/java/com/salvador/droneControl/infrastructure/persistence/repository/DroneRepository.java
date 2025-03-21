package com.salvador.droneControl.infrastructure.persistence.repository;

import com.salvador.droneControl.infrastructure.persistence.entity.DroneEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DroneRepository extends JpaRepository<DroneEntity, Long> {
}
