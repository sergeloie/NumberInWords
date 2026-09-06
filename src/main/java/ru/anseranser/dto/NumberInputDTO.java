package ru.anseranser.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import ru.anseranser.enums.Cases;
import ru.anseranser.enums.Genders;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NumberInputDTO {

    @Min(0)
    @Max(999_999_999_999L)
    private final long number;

    private final Cases theCase;

    private final Genders gender;

    @JsonCreator
    public NumberInputDTO(
            @JsonProperty("number") long number,
            @JsonProperty("case") Cases theCase,
            @JsonProperty("gender") Genders gender) {
        this.number = number;
        this.theCase = theCase != null ? theCase : Cases.NOMINATIVE;
        this.gender = gender != null ? gender : Genders.MASCULINE;
    }
}
