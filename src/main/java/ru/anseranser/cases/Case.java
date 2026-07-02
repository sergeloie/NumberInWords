package ru.anseranser.cases;

import ru.anseranser.enums.Genders;

public interface Case {
    String[] getBillions();
    String[] getMillions();
    String[] getThousands();
    String[] getHundreds();
    String[] getTeens();
    String[] getTens();
    String[] getOnes(Genders gender);
}