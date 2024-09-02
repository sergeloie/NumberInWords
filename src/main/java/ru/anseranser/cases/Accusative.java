package ru.anseranser.cases;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Accusative extends AbstractCase {

    public Accusative() {
        super("cases/accusative.json");

    }
}
