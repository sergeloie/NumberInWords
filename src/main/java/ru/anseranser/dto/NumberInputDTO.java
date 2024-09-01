package ru.anseranser.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
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
    long number;

    @NotNull
    @JsonSetter(nulls = Nulls.SKIP)
    @JsonProperty("case")
    Cases theCase = Cases.NOMINATIVE;

    @NotNull
    @JsonSetter(nulls = Nulls.SKIP)
    Genders gender = Genders.MASCULINE;

}
