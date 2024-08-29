package ru.anseranser;

public class TriDigits {
    private String[] ones = {"", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
    , "ten", "eleven", "twelve", "thirteen", "fourteen", "fifteen", "sixteen", "seventeen", "eighteen", "nineteen"};
    private String[] tens = {"", "ten", "twenty", "thirty", "forty", "fifty", "sixty", "seventy", "eighty", "ninety"};
    private String[] hundreds = {"", "oneHundred", "twoHundred", "threeHundred", "fourHundred", "fiveHundred", "sixHundred", "sevenHundred", "eightHundred", "nineHundred"};

    public String toWords(int number) {
        if (number < 0 || number >= 1000 ) {
            throw new RuntimeException("Wrong TriDigits format. Number should be three-digit positive (>= 0 or <= 999)");
        }
        int h = number / 100;
        int hRemainder = number % 100;
        int t = hRemainder / 10;
        int tRemainder = hRemainder % 10;

        StringBuilder sb = new StringBuilder();
        sb.append(hundreds[h]).append(" ");
        if (hRemainder <= 19) {
            sb.append(ones[hRemainder]);
        } else {
            sb.append(tens[t]).append(" ").append(ones[tRemainder]);
        }
        return sb.toString().trim();
    }

}
