package ru.anseranser;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.anseranser.service.TrillionProcessor;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.anseranser.enums.Cases.GENITIVE;
import static ru.anseranser.enums.Cases.NOMINATIVE;
import static ru.anseranser.enums.Genders.FEMININE;
import static ru.anseranser.enums.Genders.MASCULINE;

@SpringBootTest
class TrillionProcessorTest {

    @Autowired
    private TrillionProcessor processor;

    @Test
    void testZero() {
        assertEquals("ноль", processor.toWords(0, NOMINATIVE, MASCULINE));
    }

    @Test
    void testOneBillion() {
        assertEquals("один миллиард", processor.toWords(1_000_000_000, NOMINATIVE, MASCULINE));
    }

    @Test
    void testOneMillionOneBillion() {
        assertEquals("один миллиард один миллион", processor.toWords(1_001_000_000, NOMINATIVE, MASCULINE));
    }

    @Test
    void testOneThousand() {
        assertEquals("одна тысяча", processor.toWords(1_000, NOMINATIVE, MASCULINE));
    }

    @Test
    void testComplexNumber() {
        assertEquals("два миллиарда три миллиона четыре тысячи пять",
                     processor.toWords(2_003_004_005, NOMINATIVE, MASCULINE));
    }

    @Test
    void testFeminineGender() {
        assertEquals("одна", processor.toWords(1, NOMINATIVE, FEMININE));
    }

    @Test
    void testGenitiveCase() {
        assertEquals("двух миллиардов трёх миллионов четырёх тысяч пяти",
                     processor.toWords(2_003_004_005, GENITIVE, MASCULINE));
    }
}
