package ru.anseranser;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import static org.junit.jupiter.api.Assertions.*;

import ru.anseranser.TriDigits;

class TriDigitsTest {

    private TriDigits triDigits;

    @BeforeEach
    void setUp() {
        triDigits = new TriDigits();
    }

    @ParameterizedTest
    @CsvSource({
        "0, ''",
        "1, one",
        "10, ten",
        "11, eleven",
        "20, twenty",
        "21, twenty one",
        "100, oneHundred",
        "101, oneHundred one",
        "111, oneHundred eleven",
        "120, oneHundred twenty",
        "999, nineHundred ninety nine"
    })
    void testToWordsValidInputs(int number, String expected) {
        assertEquals(expected, triDigits.toWords(number));
    }

    @Test
    void testToWordsNegativeNumber() {
        assertThrows(RuntimeException.class, () -> triDigits.toWords(-1));
    }

    @Test
    void testToWordsNumberTooLarge() {
        assertThrows(RuntimeException.class, () -> triDigits.toWords(1000));
    }

    @Test
    void testToWordsZero() {
        assertEquals("", triDigits.toWords(0));
    }

    @Test
    void testToWordsMaxValidNumber() {
        assertEquals("nineHundred ninety nine", triDigits.toWords(999));
    }
}
