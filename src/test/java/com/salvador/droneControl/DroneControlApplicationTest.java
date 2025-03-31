package com.salvador.droneControl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

@ExtendWith(MockitoExtension.class)
class DroneControlApplicationTest {

    @Test
    void mainMethodTest() {
        try (var mock = mockStatic(SpringApplication.class)) {
            String[] args = {};
            DroneControlApplication.main(args);
            mock.verify(() -> SpringApplication.run(DroneControlApplication.class, args));
        }
    }
}