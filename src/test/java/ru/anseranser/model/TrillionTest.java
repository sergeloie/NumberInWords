package ru.anseranser.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TrillionTest {

    @Test
    void testConstructorWithValidNumber() {
        Trillion trillion = new Trillion(123456789012L);
        assertNotNull(trillion);
        assertEquals(123, trillion.getBillions().getNumber());
        assertEquals(456, trillion.getMillions().getNumber());
        assertEquals(789, trillion.getThousands().getNumber());
        assertEquals(12, trillion.getOnes().getNumber());
    }

    @ParameterizedTest
    @ValueSource(longs = {0L, 999999999999L})
    void testConstructorWithBoundaryValues(long number) {
        Trillion t = new Trillion(number);
        assertNotNull(t);
    }

    @Test
    void testConstructorWithNegativeNumber() {
        assertThrows(RuntimeException.class, () -> new Trillion(-1L));
    }

    @Test
    void testConstructorWithNumberExceedingMaximum() {
        assertThrows(RuntimeException.class, () -> new Trillion(1000000000000L));
    }

    @Test
    void testConstructorWithZero() {
        Trillion t = new Trillion(0L);
        assertEquals(0, t.getBillions().getNumber());
        assertEquals(0, t.getMillions().getNumber());
        assertEquals(0, t.getThousands().getNumber());
        assertEquals(0, t.getOnes().getNumber());
    }

    @Test
    void testConstructorWithOnlyBillions() {
        Trillion t = new Trillion(123000000000L);
        assertEquals(123, t.getBillions().getNumber());
        assertEquals(0, t.getMillions().getNumber());
        assertEquals(0, t.getThousands().getNumber());
        assertEquals(0, t.getOnes().getNumber());
    }

    @Test
    void testConstructorWithOnlyMillions() {
        Trillion t = new Trillion(123000000L);
        assertEquals(0, t.getBillions().getNumber());
        assertEquals(123, t.getMillions().getNumber());
        assertEquals(0, t.getThousands().getNumber());
        assertEquals(0, t.getOnes().getNumber());
    }

    @Test
    void testConstructorWithOnlyThousands() {
        Trillion t = new Trillion(123000L);
        assertEquals(0, t.getBillions().getNumber());
        assertEquals(0, t.getMillions().getNumber());
        assertEquals(123, t.getThousands().getNumber());
        assertEquals(0, t.getOnes().getNumber());
    }

    @Test
    void testConstructorWithOnlyOnes() {
        Trillion t = new Trillion(123L);
        assertEquals(0, t.getBillions().getNumber());
        assertEquals(0, t.getMillions().getNumber());
        assertEquals(0, t.getThousands().getNumber());
        assertEquals(123, t.getOnes().getNumber());
    }
}
