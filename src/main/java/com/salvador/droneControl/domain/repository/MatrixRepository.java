package com.salvador.droneControl.domain.repository;

import com.salvador.droneControl.domain.model.Matrix;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatrixRepository extends JpaRepository<Matrix, Long> {
}
