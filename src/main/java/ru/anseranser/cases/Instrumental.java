package ru.anseranser.cases;

import lombok.Getter;
import org.springframework.stereotype.Component;

@Getter
@Component
public class Instrumental extends AbstractCase {

    public Instrumental() {
        super("cases/instrumental.json");
    }
}
