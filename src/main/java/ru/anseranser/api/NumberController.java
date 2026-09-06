package ru.anseranser.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.anseranser.dto.NumberInputDTO;
import ru.anseranser.dto.NumberOutputDTO;
import ru.anseranser.service.WordConverter;

@RestController
@RequestMapping("/convert")
@RequiredArgsConstructor
@Tag(name = "Number Converter", description = "Convert numbers to words in Russian")
public class NumberController {

    private final WordConverter wordConverter;

    @Operation(summary = "Convert a number to its Russian word representation")
    @PostMapping
    public NumberOutputDTO convert(@Valid @RequestBody NumberInputDTO numberInputDTO) {
        return wordConverter.convert(numberInputDTO);
    }
}
