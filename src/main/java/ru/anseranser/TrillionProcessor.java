package ru.anseranser;

import ru.anseranser.cases.Case;
import ru.anseranser.cases.CaseFactory;
import ru.anseranser.enums.Cases;
import ru.anseranser.enums.Genders;
import ru.anseranser.model.Trillion;
import ru.anseranser.model.Triset;

import static ru.anseranser.enums.Genders.FEMININE;
import static ru.anseranser.enums.Genders.MASCULINE;

public class TrillionProcessor {

    public String toWords(long number, Cases caseOne, Genders gender) {
        Case theCase = CaseFactory.createCase(caseOne);
        if (number == 0) {
            return theCase.getMasculineOnes()[0];
        }
        Trillion trillion = new Trillion(number);
        StringBuilder sb = new StringBuilder();
        if (trillion.getBillions().getNumber() != 0) {
            Triset b = trillion.getBillions();
            sb.append(buildTrio(b, theCase, MASCULINE));

            sb.append(theCase.getBillions()[b.getOnes()]).append(" ");
        }

        if (trillion.getMillions().getNumber() != 0) {
            Triset m = trillion.getMillions();
            sb.append(buildTrio(m, theCase, MASCULINE));
            sb.append(theCase.getMillions()[m.getOnes()]).append(" ");
        }

        if (trillion.getThousands().getNumber() != 0) {
            Triset t = trillion.getThousands();
            sb.append(buildTrio(t, theCase, FEMININE));
            sb.append(theCase.getThousands()[t.getOnes()]).append(" ");
        }

        if (trillion.getOnes().getNumber() != 0) {
            Triset o = trillion.getOnes();
            sb.append(buildTrio(o, theCase, gender));
        }
        return sb.toString().trim();
    }

    public String buildTrio(Triset triset, Case theCase, Genders gender) {
        if (triset.getNumber() == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(triset.getHundreds() == 0 ? "" : theCase.getHundreds()[triset.getHundreds()] + " ");
        sb.append(triset.getTeens() == 0 ? "" : theCase.getTeens()[triset.getTeens()] + " ");
        sb.append(triset.getTens() == 0 ? "" : theCase.getTens()[triset.getTens()] + " ");
        sb.append(triset.getOnes() == 0 ? ""
                : switch (gender) {
            case MASCULINE -> theCase.getMasculineOnes()[triset.getOnes()] + " ";
            case FEMININE -> theCase.getFeminineOnes()[triset.getOnes()] + " ";
            case NEUTER -> theCase.getNeuterOnes()[triset.getOnes()] + " ";

        });
        return sb.toString();
    }
}
