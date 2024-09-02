package ru.anseranser.cases;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Nominative extends AbstractCase {

    public Nominative() {
        super("cases/nominative.json");
    }
}
