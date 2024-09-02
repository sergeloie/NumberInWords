package ru.anseranser.cases;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Dative extends AbstractCase {

    public Dative() {
        super("cases/dative.json");
    }
}
