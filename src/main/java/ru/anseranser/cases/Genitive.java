package ru.anseranser.cases;

import lombok.Getter;

@Getter
public class Genitive implements Case {
    public final String[] billions = {
            "миллиардов",
            "миллиарда",
            "миллиардов",
            "миллиардов",
            "миллиардов",
            "миллиардов",
            "миллиардов",
            "миллиардов",
            "миллиардов",
            "миллиардов"
    };

    public final String[] millions = {
            "миллионов",
            "миллиона",
            "миллионов",
            "миллионов",
            "миллионов",
            "миллионов",
            "миллионов",
            "миллионов",
            "миллионов",
            "миллионов"
    };

    public final String[] thousands = {
            "тысяч",
            "тысячи",
            "тысяч",
            "тысяч",
            "тысяч",
            "тысяч",
            "тысяч",
            "тысяч",
            "тысяч",
            "тысяч"
    };

    public final String[] hundreds = {
            "",
            "ста",
            "двухсот",
            "трехсот",
            "четырехсот",
            "пятисот",
            "шестисот",
            "семисот",
            "восьмисот",
            "девятисот"
    };

    public final String[] tens = {
            "",
            "десяти",
            "двадцати",
            "тридцати",
            "сорока",
            "пятидесяти",
            "шестидесяти",
            "семидесяти",
            "восьмидесяти",
            "девяноста"
    };

    public final String[] teens = {
            "", "", "", "", "", "", "", "", "", "", "",
            "одиннадцати",
            "двенадцати",
            "тринадцати",
            "четырнадцати",
            "пятнадцати",
            "шестнадцати",
            "семнадцати",
            "восемнадцати",
            "девятнадцати"
    };

    public final String[] masculineOnes = {
            "",
            "одного",
            "двух",
            "трёх",
            "четырёх",
            "пяти",
            "шести",
            "семи",
            "восьми",
            "девяти"
    };

    public final String[] feminineOnes = {
            "",
            "одной",
            "двух",
            "трёх",
            "четырёх",
            "пяти",
            "шести",
            "семи",
            "восьми",
            "девяти"
    };

    public final String[] neuterOnes = masculineOnes;
}
