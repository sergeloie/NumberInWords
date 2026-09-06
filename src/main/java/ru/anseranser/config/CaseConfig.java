package ru.anseranser.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.EnumMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import ru.anseranser.enums.Cases;
import ru.anseranser.enums.Genders;

@Configuration
public class CaseConfig {

    @Bean
    public Map<Cases, SimpleCase> cases(ObjectMapper objectMapper) throws IOException {
        return loadAll(objectMapper);
    }

    public static Map<Cases, SimpleCase> loadAll(ObjectMapper objectMapper) throws IOException {
        Map<Cases, SimpleCase> result = new EnumMap<>(Cases.class);
        for (Cases c : Cases.values()) {
            result.put(c, loadCase(objectMapper, c));
        }
        return result;
    }

    private static SimpleCase loadCase(ObjectMapper mapper, Cases caze) throws IOException {
        String filename = "cases/" + caze.name().toLowerCase() + ".json";
        ClassPathResource resource = new ClassPathResource(filename);
        return mapper.readValue(resource.getInputStream(), SimpleCase.class);
    }

    public record SimpleCase(
            String[] billions,
            String[] millions,
            String[] thousands,
            String[] hundreds,
            String[] tens,
            String[] teens,
            String[] masculineOnes,
            String[] feminineOnes,
            String[] neuterOnes) {
        public String[] getOnes(Genders gender) {
            return switch (gender) {
                case MASCULINE -> masculineOnes;
                case FEMININE -> feminineOnes;
                case NEUTER -> neuterOnes;
            };
        }
    }
}
