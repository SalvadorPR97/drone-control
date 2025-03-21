package com.salvador.droneControl.infrastructure.persistence.repository;

import com.salvador.droneControl.infrastructure.persistence.entity.MatrixEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatrixRepository extends JpaRepository<MatrixEntity, Long> {
}
