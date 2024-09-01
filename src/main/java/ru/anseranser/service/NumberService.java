package ru.anseranser.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.anseranser.dto.NumberInputDTO;
import ru.anseranser.dto.NumberOutputDTO;

@RequiredArgsConstructor
@Service
public class NumberService {

    private final TrillionProcessor tp;

    public NumberOutputDTO convert(NumberInputDTO numberInputDTO) {
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
