package ru.anseranser.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.anseranser.enums.Cases;
import ru.anseranser.enums.Genders;

public record NumberOutputDTO(long number, @JsonProperty("case") Cases theCase, Genders gender, String numberInWords) {}
