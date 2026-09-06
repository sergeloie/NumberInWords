package ru.anseranser.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.anseranser.enums.Cases.GENITIVE;
import static ru.anseranser.enums.Cases.NOMINATIVE;
import static ru.anseranser.enums.Genders.FEMININE;
import static ru.anseranser.enums.Genders.MASCULINE;

import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.anseranser.config.CaseConfig;
import ru.anseranser.config.CaseConfig.SimpleCase;
import tools.jackson.databind.json.JsonMapper;

class WordConverterTest {

    private static WordConverter converter;

    @BeforeAll
    static void setUp() throws Exception {
        Map<ru.anseranser.enums.Cases, SimpleCase> cases = CaseConfig.loadAll(new JsonMapper());
        converter = new WordConverter(cases);
    }

    @Test
    void testZero() {
        assertEquals("ноль", converter.toWords(0, NOMINATIVE, MASCULINE));
    }

    @Test
    void testOneBillion() {
        assertEquals("один миллиард", converter.toWords(1_000_000_000, NOMINATIVE, MASCULINE));
    }

    @Test
    void testOneMillionOneBillion() {
        assertEquals("один миллиард один миллион", converter.toWords(1_001_000_000, NOMINATIVE, MASCULINE));
    }

    @Test
    void testOneThousand() {
        assertEquals("одна тысяча", converter.toWords(1_000, NOMINATIVE, MASCULINE));
    }

    @Test
    void testComplexNumber() {
        // 2 003 004 005 -> 2 миллиарда 3 миллиона 4 тысячи 5
        assertEquals(
                "два миллиарда три миллиона четыре тысячи пять",
                converter.toWords(2_003_004_005, NOMINATIVE, MASCULINE));
    }

    @Test
    void testFeminineGender() {
        assertEquals("одна", converter.toWords(1, NOMINATIVE, FEMININE));
    }

    @Test
    void testGenitiveCase() {
        // 2 003 004 005 в родительном падеже
        assertEquals(
                "двух миллиардов трёх миллионов четырёх тысяч пяти",
                converter.toWords(2_003_004_005, GENITIVE, MASCULINE));
    }
}
