package com.salvador.droneControl.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrientacionTest {

    private Orientacion orientacion;

    @Test
    void turnLeft() {
        orientacion = Orientacion.N;
        Orientacion result = orientacion.turnLeft();
        assertEquals(Orientacion.O, result);
    }

    @Test
    void turnRight() {
        orientacion = Orientacion.N;
        Orientacion result = orientacion.turnRight();
        assertEquals(Orientacion.E, result);
    }
}