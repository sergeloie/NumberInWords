package ru.anseranser.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Triset {
    private int number;
    private int hundreds;
    private int tens;
//    private int teens;
    private int ones;

    public Triset(int number) {
        if (number < 0 || number > 999) {
            throw new RuntimeException("Number should be >= 0 and <= 999");
        }
        this.number = number;
        int h = number / 100;
        this.hundreds = h;
        int hRemainder = number % 100;
        if (hRemainder <= 19 && hRemainder != 10) {
            this.ones = hRemainder;
        } else {
            this.tens = hRemainder / 10;
            this.ones = hRemainder % 10;
        }

    }
}
