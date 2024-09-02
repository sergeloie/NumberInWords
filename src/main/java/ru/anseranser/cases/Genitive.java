package ru.anseranser.cases;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Genitive extends AbstractCase {

        public Genitive() {
            super("cases/genitive.json");
    }
}
