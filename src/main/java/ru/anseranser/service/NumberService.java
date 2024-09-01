package ru.anseranser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.anseranser.TrillionProcessor;
import ru.anseranser.dto.NumberInputDTO;
import ru.anseranser.dto.NumberOutputDTO;

@RequiredArgsConstructor
@Service
public class NumberService {
    public NumberOutputDTO convert(NumberInputDTO numberInputDTO) {
        TrillionProcessor tp = new TrillionProcessor();
        String result = tp.toWords(
                numberInputDTO.getNumber(),
                numberInputDTO.getTheCase(),
                numberInputDTO.getGender()
        );
        return new NumberOutputDTO(
                numberInputDTO.getNumber(),
                numberInputDTO.getTheCase(),
                numberInputDTO.getGender(),
                result
        );
    }
}
