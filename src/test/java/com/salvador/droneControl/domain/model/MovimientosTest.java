package com.salvador.droneControl.domain.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class MovimientosTest {

    @Test
    void testEnumValues() {
        // Verifica que los valores existen
        assertEquals(3, Movimientos.values().length);
        assertEquals(Movimientos.TURN_LEFT, Movimientos.valueOf("TURN_LEFT"));
        assertEquals(Movimientos.TURN_RIGHT, Movimientos.valueOf("TURN_RIGHT"));
        assertEquals(Movimientos.MOVE_FORWARD, Movimientos.valueOf("MOVE_FORWARD"));
    }

    @Test
    void testEnumToString() {
        // Verifica que el toString devuelve los valores esperados
        assertEquals("TURN_LEFT", Movimientos.TURN_LEFT.toString());
        assertEquals("TURN_RIGHT", Movimientos.TURN_RIGHT.toString());
        assertEquals("MOVE_FORWARD", Movimientos.MOVE_FORWARD.toString());
    }
}