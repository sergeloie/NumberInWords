package ru.anseranser.model;

import org.junit.jupiter.api.Test;
import ru.anseranser.exception.NumberOutOfBoundException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TrisetTest {

    @Test
    public void testTrisetConstructorValidInput() {
        Triset triset = new Triset(123);
        assertEquals(123, triset.getNumber());
        assertEquals(1, triset.getHundreds());
        assertEquals(2, triset.getTens());
        assertEquals(3, triset.getOnes());
        assertEquals(0, triset.getTeens());
    }

    @Test
    public void testTrisetConstructorWithTeens() {
        Triset triset = new Triset(215);
        assertEquals(215, triset.getNumber());
        assertEquals(2, triset.getHundreds());
        assertEquals(0, triset.getTens());
        assertEquals(0, triset.getOnes());
        assertEquals(15, triset.getTeens());
    }

    @Test
    public void testTrisetConstructorZero() {
        Triset triset = new Triset(0);
        assertEquals(0, triset.getNumber());
        assertEquals(0, triset.getHundreds());
        assertEquals(0, triset.getTens());
        assertEquals(0, triset.getOnes());
        assertEquals(0, triset.getTeens());
    }

    @Test
    public void testTrisetConstructorMaxValue() {
        Triset triset = new Triset(999);
        assertEquals(999, triset.getNumber());
        assertEquals(9, triset.getHundreds());
        assertEquals(9, triset.getTens());
        assertEquals(9, triset.getOnes());
        assertEquals(0, triset.getTeens());
    }

    @Test
    public void testTrisetConstructorNegativeNumber() {
        assertThrows(NumberOutOfBoundException.class, () -> new Triset(-1));
    }

    @Test
    public void testTrisetConstructorNumberTooLarge() {
        assertThrows(NumberOutOfBoundException.class, () -> new Triset(1000));
    }
}
