package ru.anseranser.model;

import lombok.Getter;
import ru.anseranser.exception.NumberOutOfBoundException;

@Getter
public class Triset {
    private int number;
    private int hundreds;
    private int tens;
    private int teens;
    private int ones;

    public Triset(int number) {
        if (number < 0 || number > 999) {
            throw new NumberOutOfBoundException("Number should be >= 0 and <= 999");
        }
        this.number = number;
        this.hundreds = number / 100;
        int hRemainder = number % 100;
        if (hRemainder >= 11 && hRemainder <= 19) {
            this.teens = hRemainder;
        } else {
            this.tens = hRemainder / 10;
            this.ones = hRemainder % 10;
        }
    }
}
