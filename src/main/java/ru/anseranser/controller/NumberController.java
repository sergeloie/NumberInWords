package ru.anseranser.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.anseranser.dto.NumberInputDTO;
import ru.anseranser.dto.NumberOutputDTO;
import ru.anseranser.service.NumberService;

@RestController
@RequestMapping("/convert")
@RequiredArgsConstructor
public class NumberController {

    private final NumberService numberService;

    @PostMapping
    public NumberOutputDTO convert(@RequestBody NumberInputDTO numberInputDTO) {
        return numberService.convert(numberInputDTO);
    }

}

