package ru.anseranser.cases;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.anseranser.enums.Cases;

@Component
public final class CaseFactory {

    private final ApplicationContext applicationContext;

    public CaseFactory(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public Case createCase(Cases theCase) {
        return switch (theCase) {
            case NOMINATIVE -> applicationContext.getBean(Nominative.class);
            case GENITIVE -> applicationContext.getBean(Genitive.class);
            case DATIVE -> applicationContext.getBean(Dative.class);
            case ACCUSATIVE -> applicationContext.getBean(Accusative.class);
            case INSTRUMENTAL -> applicationContext.getBean(Instrumental.class);
            case PREPOSITIONAL -> applicationContext.getBean(Prepositional.class);
        };
    }
}



