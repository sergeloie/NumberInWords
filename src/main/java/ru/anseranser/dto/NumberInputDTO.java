package ru.anseranser.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ru.anseranser.enums.Cases;
import ru.anseranser.enums.Genders;

@NoArgsConstructor
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NumberInputDTO {

    @Min(0)
    @Max(999_999_999_999L)
    private long number;

    @JsonProperty("case")
    @JsonSetter(nulls = Nulls.SKIP)
    private Cases theCase = Cases.NOMINATIVE;

    @JsonSetter(nulls = Nulls.SKIP)
    private Genders gender = Genders.MASCULINE;

}
