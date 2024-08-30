package ru.anseranser.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class Triset {
    private int hundreds;
    private int tens;
    private int teens;
    private int ones;

    public Triset(int number) {
        if (number < 0 || number > 999) {
            throw new RuntimeException("Number should be >= 0 and <= 999");
        }
        int h = number / 100;
        this.hundreds = h;
        int hRemainder = number % 100;
        if (hRemainder <= 9) {
            this.ones = hRemainder;
        } else if (hRemainder >= 11 && hRemainder <= 19) {
            this.teens = hRemainder;
        } else {
            int t = hRemainder / 10;
            int o = hRemainder % 10;
            this.tens = t;
            this.ones = o;
        }
    }
}
