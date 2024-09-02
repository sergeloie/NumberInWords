package ru.anseranser.cases;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Prepositional extends AbstractCase {

    public Prepositional() {
        super("cases/prepositional.json");
    }
}
