package ru.anseranser.cases;

import lombok.Getter;

@Getter
public class Instrumental implements Case {
    private final String[] billions = {
            "миллиардами",
            "миллиардом",
            "миллиардами",
            "миллиардами",
            "миллиардами",
            "миллиардами",
            "миллиардами",
            "миллиардами",
            "миллиардами",
            "миллиардами"
    };

    private final String[] millions = {
            "миллионами",
            "миллионом",
            "миллионами",
            "миллионами",
            "миллионами",
            "миллионами",
            "миллионами",
            "миллионами",
            "миллионами",
            "миллионами"
    };

    private final String[] thousands = {
            "тысячами",
            "тысячей",
            "тысячами",
            "тысячами",
            "тысячами",
            "тысячами",
            "тысячами",
            "тысячами",
            "тысячами",
            "тысячами"
    };

    private final String[] hundreds = {
            "",
            "ста",
            "двумястами",
            "тремястами",
            "четырьмястами",
            "пятьюстами",
            "шестьюстами",
            "семьюстами",
            "восемьюстами",
            "девятьюстами"
    };

    private final String[] tens = {
            "",
            "десятью",
            "двадцатью",
            "тридцатью",
            "сорока",
            "пятьюдесятью",
            "шестьюдесятью",
            "семьюдесятью",
            "восьмьюдесятью",
            "девяноста"
    };

    private final String[] teens = {
            "", "", "", "", "", "", "", "", "", "", "",
            "одиннадцатью",
            "двенадцатью",
            "тринадцатью",
            "четырнадцатью",
            "пятнадцатью",
            "шестнадцатью",
            "семнадцатью",
            "восемнадцатью",
            "девятнадцатью"
    };

    private final String[] masculineOnes = {
            "нолём",
            "одним",
            "двумя",
            "тремя",
            "четырьмя",
            "пятью",
            "шестью",
            "семью",
            "восемью",
            "девятью"
    };

    private final String[] feminineOnes = {
            "нолём",
            "одной",
            "двумя",
            "тремя",
            "четырьмя",
            "пятью",
            "шестью",
            "семью",
            "восемью",
            "девятью"
    };

    private final String[] neuterOnes = masculineOnes;
}
