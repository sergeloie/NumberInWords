package ru.anseranser.cases;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import ru.anseranser.exception.CaseFileNotFound;

import java.io.IOException;

@Getter
public abstract class AbstractCase implements Case {

    protected final String[] billions;
    protected final String[] millions;
    protected final String[] thousands;
    protected final String[] hundreds;
    protected final String[] tens;
    protected final String[] teens;
    protected final String[] masculineOnes;
    protected final String[] feminineOnes;
    protected final String[] neuterOnes;

    public AbstractCase(String filename) {

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = objectMapper.readTree(this.getClass()
                    .getClassLoader()
                    .getResourceAsStream(filename));
        } catch (IOException e) {
            throw new CaseFileNotFound("File with case definition not found: " + filename);
        }

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
