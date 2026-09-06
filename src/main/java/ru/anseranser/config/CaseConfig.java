package ru.anseranser.config;

import java.io.IOException;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import ru.anseranser.enums.Cases;
import ru.anseranser.enums.Genders;
import tools.jackson.databind.ObjectMapper;

@Configuration
public class CaseConfig {

    @Bean
    public Map<Cases, SimpleCase> cases(ObjectMapper objectMapper) throws IOException {
        return loadAll(objectMapper);
    }

    @SuppressWarnings("PMD.UseConcurrentHashMap") // EnumMap fits enum keys best; populated once, read-only afterwards
    public static Map<Cases, SimpleCase> loadAll(ObjectMapper objectMapper) throws IOException {
        Map<Cases, SimpleCase> result = new EnumMap<>(Cases.class);
        for (Cases c : Cases.values()) {
            result.put(c, loadCase(objectMapper, c));
        }
        return result;
    }

    private static SimpleCase loadCase(ObjectMapper mapper, Cases caze) throws IOException {
        String filename = "cases/" + caze.name().toLowerCase(Locale.ROOT) + ".json";
        ClassPathResource resource = new ClassPathResource(filename);
        return mapper.readValue(resource.getInputStream(), SimpleCase.class);
    }

    public record SimpleCase(
            List<String> billions,
            List<String> millions,
            List<String> thousands,
            List<String> hundreds,
            List<String> tens,
            List<String> teens,
            List<String> masculineOnes,
            List<String> feminineOnes,
            List<String> neuterOnes) {
        public SimpleCase {
            billions = List.copyOf(billions);
            millions = List.copyOf(millions);
            thousands = List.copyOf(thousands);
            hundreds = List.copyOf(hundreds);
            tens = List.copyOf(tens);
            teens = List.copyOf(teens);
            masculineOnes = List.copyOf(masculineOnes);
            feminineOnes = List.copyOf(feminineOnes);
            neuterOnes = List.copyOf(neuterOnes);
        }

        public List<String> getOnes(Genders gender) {
            return switch (gender) {
                case MASCULINE -> masculineOnes;
                case FEMININE -> feminineOnes;
                case NEUTER -> neuterOnes;
            };
        }
    }
}
