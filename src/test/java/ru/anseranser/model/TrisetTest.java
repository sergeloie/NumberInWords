package ru.anseranser.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TrisetTest {

    @Test
    public void TestCreate() {
        Triset t123 = new Triset(123);
        assertEquals(1, t123.getHundreds());
        assertEquals(2, t123.getTens());
        assertEquals(3, t123.getOnes());
        assertEquals(0, t123.getTeens());

        Triset t219 = new Triset(219);
        assertEquals(2, t219.getHundreds());
        assertEquals(19, t219.getTeens());
        assertEquals(0, t219.getTens());
        assertEquals(0, t219.getOnes());

        Triset t910 = new Triset(910);
        assertEquals(9, t910.getHundreds());
        assertEquals(1, t910.getTens());
        assertEquals(0, t910.getTeens());
        assertEquals(0, t910.getOnes());
    }


    @Test
    public void testNegativeNumber() {
        assertThrows(RuntimeException.class, () -> new Triset(-1));
    }

    @Test
    public void testNumberGreaterThan999() {
        assertThrows(RuntimeException.class, () -> new Triset(1000));
    }

    @Test
    public void testBoundaryValue1() {
        Triset t001 = new Triset(1);
        assertEquals(0, t001.getHundreds());
        assertEquals(0, t001.getTens());
        assertEquals(1, t001.getOnes());
        assertEquals(0, t001.getTeens());
    }

    @Test
    public void testBoundaryValue999() {
        Triset t999 = new Triset(999);
        assertEquals(9, t999.getHundreds());
        assertEquals(9, t999.getTens());
        assertEquals(9, t999.getOnes());
        assertEquals(0, t999.getTeens());
    }

    @Test
    public void testMixedValues() {
        Triset t456 = new Triset(456);
        assertEquals(4, t456.getHundreds());
        assertEquals(5, t456.getTens());
        assertEquals(6, t456.getOnes());
        assertEquals(0, t456.getTeens());
    }

    @Test
    public void testZero() {
        Triset t000 = new Triset(0);
        assertEquals(0, t000.getHundreds());
        assertEquals(0, t000.getTens());
        assertEquals(0, t000.getOnes());
        assertEquals(0, t000.getTeens());
    }

    @Test
    public void testMaxValue() {
        Triset t999 = new Triset(999);
        assertEquals(9, t999.getHundreds());
        assertEquals(9, t999.getTens());
        assertEquals(9, t999.getOnes());
        assertEquals(0, t999.getTeens());
    }

    @Test
    public void testAllTeens() {
        for (int i = 11; i <= 19; i++) {
            Triset t = new Triset(i);
            assertEquals(0, t.getHundreds());
            assertEquals(i, t.getTeens());
            assertEquals(0, t.getTens());
            assertEquals(0, t.getOnes());
        }
    }

    @Test
    public void testOnlyOnes() {
        Triset t007 = new Triset(7);
        assertEquals(0, t007.getHundreds());
        assertEquals(0, t007.getTens());
        assertEquals(7, t007.getOnes());
        assertEquals(0, t007.getTeens());
    }

    @Test
    public void testOnlyTens() {
        Triset t050 = new Triset(50);
        assertEquals(0, t050.getHundreds());
        assertEquals(5, t050.getTens());
        assertEquals(0, t050.getOnes());
        assertEquals(0, t050.getTeens());
    }

    @Test
    public void testOnlyHundreds() {
        Triset t500 = new Triset(500);
        assertEquals(5, t500.getHundreds());
        assertEquals(0, t500.getTens());
        assertEquals(0, t500.getOnes());
        assertEquals(0, t500.getTeens());
    }
}
