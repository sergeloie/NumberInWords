package ru.anseranser.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

    class TrillionTest {

        private Trillion trillion;

        @Test
        void testConstructorWithZero() {
            trillion = new Trillion(0);
            assertEquals(new Triset(0), trillion.getBillions());
            assertEquals(new Triset(0), trillion.getMillions());
            assertEquals(new Triset(0), trillion.getThousands());
            assertEquals(new Triset(0), trillion.getOnes());
        }

        @Test
        void testConstructorWithMaxValue() {
            trillion = new Trillion(999_999_999_999L);
            assertEquals(new Triset(999), trillion.getBillions());
            assertEquals(new Triset(999), trillion.getMillions());
            assertEquals(new Triset(999), trillion.getThousands());
            assertEquals(new Triset(999), trillion.getOnes());
        }

        @Test
        void testConstructorWithValidValues() {
            long number = 157_485_336_720L;
            trillion = new Trillion(number);

            assertEquals(new Triset(157), trillion.getBillions());
            assertEquals(new Triset(485), trillion.getMillions());
            assertEquals(new Triset(336), trillion.getThousands());
            assertEquals(new Triset(720), trillion.getOnes());
        }

        @Test
        void testConstructorWithNegativeNumber() {
            assertThrows(RuntimeException.class, () -> new Trillion(-1));
        }

        @Test
        void testConstructorWithNumberExceedingMaxValue() {
            assertThrows(RuntimeException.class, () -> new Trillion(1_000_000_000_000L));
        }
    }
