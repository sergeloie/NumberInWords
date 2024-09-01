package ru.anseranser.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.anseranser.enums.Cases;
import ru.anseranser.enums.Genders;

@AllArgsConstructor
@Getter
public class NumberOutputDTO {

    private long number;
    @JsonProperty("case")
    private Cases theCase;
    private Genders gender;
    private String numberInWords;

}
