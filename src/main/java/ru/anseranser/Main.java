package ru.anseranser;

import ru.anseranser.model.Triset;

import static ru.anseranser.enums.Cases.DATIVE;
import static ru.anseranser.enums.Cases.GENITIVE;
import static ru.anseranser.enums.Cases.INSTRUMENTAL;
import static ru.anseranser.enums.Cases.NOMINATIVE;
import static ru.anseranser.enums.Cases.PREPOSITIONAL;
import static ru.anseranser.enums.Genders.FEMININE;
import static ru.anseranser.enums.Genders.MASCULINE;
import static ru.anseranser.enums.Genders.NEUTER;

public class Main {
    public static void main(String[] args) {
        TrillionProcessor tp = new TrillionProcessor();
        String result = tp.toWords(120451789121L,PREPOSITIONAL, FEMININE);
        String result2 = tp.toWords(122453781121L,INSTRUMENTAL, NEUTER);
        String result3 = tp.toWords(124456582121L,DATIVE, MASCULINE);
        String result4 = tp.toWords(101211321431L,GENITIVE, FEMININE);
        System.out.println(result);
        System.out.println(result2);
        System.out.println(result3);
        System.out.println(result4);
    }

}