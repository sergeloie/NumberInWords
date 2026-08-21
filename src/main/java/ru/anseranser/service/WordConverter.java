package ru.anseranser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.anseranser.cases.CaseConfig.SimpleCase;
import ru.anseranser.dto.NumberInputDTO;
import ru.anseranser.dto.NumberOutputDTO;
import ru.anseranser.enums.Cases;
import ru.anseranser.enums.Genders;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WordConverter {

    private final Map<Cases, SimpleCase> cases;

    public String toWords(long number, Cases caze, Genders gender) {
        if (number == 0) {
            return cases.get(caze).getOnes(Genders.MASCULINE)[0];
        }

        SimpleCase theCase = cases.get(caze);
        List<String> parts = new ArrayList<>();

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
                parts.add(trioWords + unitSuffix);
            }
            remaining /= 1000;
            trioIndex++;
        }
        Collections.reverse(parts);
        return String.join(" ", parts);
    }

    private static int onesDigit(int trio) {
        int r = trio % 100;
        return (r >= 11 && r <= 19) ? 0 : r % 10;
    }

    private String buildTrio(int trio, SimpleCase theCase, Genders gender) {
        int hundreds = trio / 100;
        int remainder = trio % 100;
        boolean isTeens = remainder >= 11 && remainder <= 19;
        int teens = isTeens ? remainder : 0;
        int tens = isTeens ? 0 : remainder / 10;
        int ones = isTeens ? 0 : remainder % 10;

        List<String> parts = new ArrayList<>();
        if (hundreds > 0) {
            parts.add(theCase.hundreds()[hundreds]);
        }
        if (teens > 0) {
            parts.add(theCase.teens()[teens]);
        } else {
            if (tens > 0) {
                parts.add(theCase.tens()[tens]);
            }
            if (ones > 0) {
                parts.add(theCase.getOnes(gender)[ones]);
            }
        }
        return String.join(" ", parts);
    }

    private String getUnitSuffix(SimpleCase theCase, int trioIndex, int trio) {
        int ones = onesDigit(trio);
        String suffix = switch (trioIndex) {
            case 1 -> theCase.thousands()[ones]; // thousands
            case 2 -> theCase.millions()[ones];  // millions
            case 3 -> theCase.billions()[ones];  // billions
            default -> "";
        };
        return suffix.isEmpty() ? "" : " " + suffix;
    }

    public NumberOutputDTO convert(NumberInputDTO input) {
        String result = toWords(input.getNumber(), input.getTheCase(), input.getGender());
        return new NumberOutputDTO(input.getNumber(), input.getTheCase(), input.getGender(), result);
    }
}
