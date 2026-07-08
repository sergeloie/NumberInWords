package ru.anseranser.cases;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import ru.anseranser.enums.Cases;
import ru.anseranser.enums.Genders;

import java.io.IOException;
import java.util.Map;

@Configuration
public class CaseConfig {

    @Bean
    public Map<Cases, Case> cases(ObjectMapper objectMapper) throws IOException {
        Map<Cases, Case> result = new java.util.EnumMap<>(Cases.class);
        for (Cases c : Cases.values()) {
            result.put(c, loadCase(objectMapper, c));
        }
        return result;
    }

    private Case loadCase(ObjectMapper mapper, Cases caze) throws IOException {
        String filename = "cases/" + caze.name().toLowerCase() + ".json";
        ClassPathResource resource = new ClassPathResource(filename);
        CaseData data = mapper.readValue(resource.getInputStream(), CaseData.class);
        return new SimpleCase(data);
    }

    // Internal data holder matching JSON structure
    public record CaseData(
            String[] billions,
            String[] millions,
            String[] thousands,
            String[] hundreds,
            String[] tens,
            String[] teens,
            String[] masculineOnes,
            String[] feminineOnes,
            String[] neuterOnes
    ) {
    }

    // Simple implementation of Case interface
    private static class SimpleCase implements Case {
        private final String[] billions;
        private final String[] millions;
        private final String[] thousands;
        private final String[] hundreds;
        private final String[] teens;
        private final String[] tens;
        private final String[] masculineOnes;
        private final String[] feminineOnes;
        private final String[] neuterOnes;

        SimpleCase(CaseData data) {
            this.billions = data.billions();
            this.millions = data.millions();
            this.thousands = data.thousands();
            this.hundreds = data.hundreds();
            this.teens = data.teens();
            this.tens = data.tens();
            this.masculineOnes = data.masculineOnes();
            this.feminineOnes = data.feminineOnes();
            this.neuterOnes = data.neuterOnes();
        }

        @Override
        public String[] getBillions() {
            return billions;
        }

        @Override
        public String[] getMillions() {
            return millions;
        }

        @Override
        public String[] getThousands() {
            return thousands;
        }

        @Override
        public String[] getHundreds() {
            return hundreds;
        }

        @Override
        public String[] getTeens() {
            return teens;
        }

        @Override
        public String[] getTens() {
            return tens;
        }

        @Override
        public String[] getOnes(Genders gender) {
            return switch (gender) {
                case MASCULINE -> masculineOnes;
                case FEMININE -> feminineOnes;
                case NEUTER -> neuterOnes;
            };
        }
    }
}
