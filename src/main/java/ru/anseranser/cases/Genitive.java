package ru.anseranser.cases;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.SneakyThrows;

import java.io.IOException;

@Getter
public class Genitive implements Case {

    private final String[] billions;
    private final String[] millions;
    private final String[] thousands;
    private final String[] hundreds;
    private final String[] tens;
    private final String[] teens;
    private final String[] masculineOnes;
    private final String[] feminineOnes;
    private final String[] neuterOnes;

    @SneakyThrows(IOException.class)
    public Genitive() {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(this.getClass()
                .getClassLoader()
                .getResourceAsStream("cases/genitive.json"));

        this.billions = objectMapper.convertValue(jsonNode.get("billions"), String[].class);
        this.millions = objectMapper.convertValue(jsonNode.get("millions"), String[].class);
        this.thousands = objectMapper.convertValue(jsonNode.get("thousands"), String[].class);
        this.hundreds = objectMapper.convertValue(jsonNode.get("hundreds"), String[].class);
        this.tens = objectMapper.convertValue(jsonNode.get("tens"), String[].class);
        this.teens = objectMapper.convertValue(jsonNode.get("teens"), String[].class);
        this.masculineOnes = objectMapper.convertValue(jsonNode.get("masculineOnes"), String[].class);
        this.feminineOnes = objectMapper.convertValue(jsonNode.get("feminineOnes"), String[].class);
        this.neuterOnes = objectMapper.convertValue(jsonNode.get("neuterOnes"), String[].class);
    }
}
