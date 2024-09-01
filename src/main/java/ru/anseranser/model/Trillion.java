package ru.anseranser.model;

import lombok.Getter;
import ru.anseranser.exception.NumberOutOfBoundException;

@Getter
public class Trillion {
    private Triset billions;
    private Triset millions;
    private Triset thousands;
    private Triset ones;

    public Trillion(long number) {
        if (number < 0 || number > 999_999_999_999L) {
            throw new NumberOutOfBoundException("Number should be >= 0 and <= 999_999_999_999");
        }
        long b = number / 1_000_000_000;
        long bRemainder = number % 1_000_000_000;

        long m = bRemainder / 1_000_000;
        long mRemainder = bRemainder % 1_000_000;

        long t = mRemainder / 1000;
        long o = mRemainder % 1000;

        this.billions = new Triset((int) b);
        this.millions = new Triset((int) m);
        this.thousands = new Triset((int) t);
        this.ones = new Triset((int) o);
    }
}
