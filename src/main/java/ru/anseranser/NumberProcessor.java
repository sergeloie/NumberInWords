package ru.anseranser;

public class NumberProcessor {
    private String[] oneThousandPower = {"billion", "million", "thousand"};
    private String[] manyThousandsPower = {"billions", "millions", "thousands"};

    public String toWords(long number) {
        if (number < 0 || number > 999_999_999_999L) {
            throw new RuntimeException("Wrong number format. Number should be >= 0 or <= 999_999_999_999)");
        }

        int b = Math.toIntExact(number / 1_000_000_000);
        int bRemainder = Math.toIntExact(number % 1_000_000_000);

        int m = bRemainder / 1_000_000;
        int mRemainder = bRemainder % 1_000_000;

        int t = mRemainder / 1000;
        int tRemainder = mRemainder % 1000;

        TriDigits triDigits = new TriDigits();

        StringBuilder sb = new StringBuilder();
        if (b > 0) {
            sb.append(triDigits.toWords(b)).append(" ").append(oneThousandPower[0]).append(" ");
        }
        if (m > 0) {
            sb.append(triDigits.toWords(m)).append(" ").append(oneThousandPower[1]).append(" ");
        }
        if (t > 0) {
            sb.append(triDigits.toWords(t)).append(" ").append(oneThousandPower[2]).append(" ");
        }
        sb.append(triDigits.toWords(tRemainder));
        return sb.toString().trim();

    }
}
