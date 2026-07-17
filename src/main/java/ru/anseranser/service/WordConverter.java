package ru.anseranser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.anseranser.cases.Case;
import ru.anseranser.dto.NumberInputDTO;
import ru.anseranser.dto.NumberOutputDTO;
import ru.anseranser.enums.Cases;
import ru.anseranser.enums.Genders;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WordConverter {

    private final Map<Cases, Case> cases;

    public String toWords(long number, Cases caze, Genders gender) {
        if (number == 0) {
            return cases.get(caze).getOnes(Genders.MASCULINE)[0];
        }

        Case theCase = cases.get(caze);
        StringBuilder sb = new StringBuilder();

        int trioIndex = 0;
        long remaining = number;
        while (remaining > 0) {
            int trio = (int) (remaining % 1000);
            if (trio > 0) {
                Genders trioGender = switch (trioIndex) {
                    case 0 -> gender;              // ones - use passed gender
                    case 1 -> Genders.FEMININE;    // thousands - always feminine
                    default -> Genders.MASCULINE;  // millions, billions - always masculine
                };
                String trioWords = buildTrio(trio, theCase, trioGender);
                String unitSuffix = getUnitSuffix(theCase, trioIndex, trio);
                sb.insert(0, trioWords + unitSuffix + " ");
            }
            remaining /= 1000;
            trioIndex++;
        }
        return sb.toString().trim();
    }

    private String buildTrio(int trio, Case theCase, Genders gender) {
        int hundreds = trio / 100;
        int remainder = trio % 100;
        boolean isTeens = remainder >= 11 && remainder <= 19;
        int teens = isTeens ? remainder : 0;
        int tens = isTeens ? 0 : remainder / 10;
        int ones = isTeens ? 0 : remainder % 10;

        List<String> parts = new ArrayList<>();
        if (hundreds > 0) {
            parts.add(theCase.getHundreds()[hundreds]);
        }
        if (teens > 0) {
            parts.add(theCase.getTeens()[teens]);
        } else {
            if (tens > 0) {
                parts.add(theCase.getTens()[tens]);
            }
            if (ones > 0) {
                parts.add(theCase.getOnes(gender)[ones]);
            }
        }
        return String.join(" ", parts);
    }

    private String getUnitSuffix(Case theCase, int trioIndex, int trio) {
        int ones = trio % 100 >= 11 && trio % 100 <= 19 ? 0 : trio % 10;
        String suffix = switch (trioIndex) {
            case 1 -> theCase.getThousands()[ones]; // thousands
            case 2 -> theCase.getMillions()[ones];  // millions
            case 3 -> theCase.getBillions()[ones];  // billions
            default -> "";
        };
        return suffix.isEmpty() ? "" : " " + suffix;
    }

    public NumberOutputDTO convert(NumberInputDTO input) {
        String result = toWords(input.getNumber(), input.getTheCase(), input.getGender());
        return new NumberOutputDTO(input.getNumber(), input.getTheCase(), input.getGender(), result);
    }
}
