package ru.anseranser;

public class Main {
    public static void main(String[] args) {
        TrillionProcessor tp = new TrillionProcessor();
        String result = tp.toWords(123456789123L);
        String result2 = tp.toWords(123456781123L);
        String result3 = tp.toWords(123456782123L);
        System.out.println(result);
        System.out.println(result2);
        System.out.println(result3);
    }

}