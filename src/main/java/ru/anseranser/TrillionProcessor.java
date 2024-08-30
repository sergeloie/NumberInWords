package ru.anseranser;

import ru.anseranser.model.Trillion;
import ru.anseranser.model.Triset;

public class TrillionProcessor {
    public final String[] billions = {
            "миллиардов",
            "миллиард",
            "миллиарда",
            "миллиарда",
            "миллиарда",
            "миллиардов",
            "миллиардов",
            "миллиардов",
            "миллиардов",
            "миллиардов"
    };

    public final String[] millions = {
            "миллионов",
            "миллион",
            "миллиона",
            "миллиона",
            "миллиона",
            "миллионов",
            "миллионов",
            "миллионов",
            "миллионов",
            "миллионов"
    };

    public final String[] thousands = {
            "тысяч",
            "тысяча",
            "тысячи",
            "тысячи",
            "тысячи",
            "тысяч",
            "тысяч",
            "тысяч",
            "тысяч",
            "тысяч"
    };

    public final String[] hundreds = {
            "",
            "сто",
            "двести",
            "триста",
            "четыреста",
            "пятьсот",
            "шестьсот",
            "семьсот",
            "восемьсот",
            "девятьсот"
    };

    public final String[] tens = {
            "",
            "десять",
            "двадцать",
            "тридцать",
            "сорок",
            "пятьдесят",
            "шестьдесят",
            "семьдесят",
            "восемьдесят",
            "девяносто"
    };

    public final String[] ones = {
            "", "один", "два", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять", "десять",
            "одиннадцать",
            "двенадцать",
            "тринадцать",
            "четырнадцать",
            "пятнадцать",
            "шестнадцать",
            "семнадцать",
            "восемнадцать",
            "девятнадцать"
    };

    public final String[] masculineOnes = {"", "один", "два", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять"};
    public final String[] feminineOnes = {"", "одна", "две", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять"};
    public final String[] neuterOnes = {"", "одно", "два", "три", "четыре", "пять", "шесть", "семь", "восемь", "девять"};

    public String toWords(long number) {
        Trillion trillion = new Trillion(number);
        StringBuilder sb = new StringBuilder();
        if (trillion.getBillions().getNumber() != 0) {
            Triset b = trillion.getBillions();
            sb.append(b.getHundreds() == 0 ? "" : hundreds[b.getHundreds()] + " ");
            sb.append(b.getTens() == 0 ? "" : tens[b.getTens()] + " ");
            sb.append(b.getOnes() == 0 ? "" : ones[b.getOnes()] + " ");
            sb.append(billions[b.getNumber() % 10]).append(" ");
        }

        if (trillion.getMillions().getNumber() != 0) {
            Triset m = trillion.getMillions();
            sb.append(m.getHundreds() == 0 ? "" : hundreds[m.getHundreds()] + " ");
            sb.append(m.getTens() == 0 ? "" : tens[m.getTens()] + " ");
            sb.append(m.getOnes() == 0 ? "" : ones[m.getOnes()] + " ");
            sb.append(millions[m.getNumber() % 10]).append(" ");
        }

        if (trillion.getThousands().getNumber() != 0) {
            Triset t = trillion.getThousands();
            sb.append(t.getHundreds() == 0 ? "" : hundreds[t.getHundreds()] + " ");
            sb.append(t.getTens() == 0 ? "" : tens[t.getTens()] + " ");
            sb.append(t.getOnes() == 0 ? "" : feminineOnes[t.getOnes()] + " ");
            sb.append(thousands[t.getNumber() % 10]).append(" ");
        }

        if (trillion.getOnes().getNumber() != 0) {
            Triset o = trillion.getOnes();
            sb.append(o.getHundreds() == 0 ? "" : hundreds[o.getHundreds()] + " ");
            sb.append(o.getTens() == 0 ? "" : tens[o.getTens()] + " ");
            sb.append(o.getOnes() == 0 ? "" : ones[o.getOnes()] + " ");
        }
        return sb.toString();
    }
}
