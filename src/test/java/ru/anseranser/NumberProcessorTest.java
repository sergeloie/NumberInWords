package ru.anseranser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import ru.anseranser.NumberProcessor;

import static org.junit.jupiter.api.Assertions.*;

class NumberProcessorTest {

    private NumberProcessor numberProcessor;

    @BeforeEach
    void setUp() {
        numberProcessor = new NumberProcessor();
    }

    @Test
    void testToWordsZero() {
        assertEquals("", numberProcessor.toWords(0));
    }

    @Test
    void testToWordsNegativeNumber() {
        assertThrows(RuntimeException.class, () -> numberProcessor.toWords(-1));
    }

    @Test
    void testToWordsExceedingMaximum() {
        assertThrows(RuntimeException.class, () -> numberProcessor.toWords(1_000_000_000_000L));
    }

    @ParameterizedTest
    @CsvSource({
        "1, one",
        "21, twenty one",
        "105, oneHundred five",
        "1000, one thousand",
        "1000000, one million",
        "1000000000, one billion",
        "999999999999, nineHundred ninety nine billion nineHundred ninety nine million nineHundred ninety nine thousand nineHundred ninety nine"
    })
    void testToWordsVariousNumbers(long input, String expected) {
        assertEquals(expected, numberProcessor.toWords(input));
    }

    @Test
    void testToWordsMixedScales() {
        assertEquals("one billion one million one thousand one", numberProcessor.toWords(1_001_001_001));
    }

    @Test
    void testToWordsAllZeros() {
        assertEquals("one billion", numberProcessor.toWords(1_000_000_000));
    }

    @Test
    void testToWordsMaximumValue() {
        assertEquals("nineHundred ninety nine billion nineHundred ninety nine million nineHundred ninety nine thousand nineHundred ninety nine", numberProcessor.toWords(999_999_999_999L));
    }
}
