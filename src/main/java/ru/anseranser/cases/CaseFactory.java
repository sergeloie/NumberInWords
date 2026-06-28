package ru.anseranser.cases;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import ru.anseranser.enums.Cases;

import java.util.Map;
import java.util.function.Supplier;

@Component
public final class CaseFactory {

    private final Map<Cases, Supplier<Case>> caseMap;

    public CaseFactory(ApplicationContext applicationContext) {
        this.caseMap = Map.of(
                Cases.NOMINATIVE, () -> applicationContext.getBean(Nominative.class),
                Cases.GENITIVE, () -> applicationContext.getBean(Genitive.class),
                Cases.DATIVE, () -> applicationContext.getBean(Dative.class),
                Cases.ACCUSATIVE, () -> applicationContext.getBean(Accusative.class),
                Cases.INSTRUMENTAL, () -> applicationContext.getBean(Instrumental.class),
                Cases.PREPOSITIONAL, () -> applicationContext.getBean(Prepositional.class)
        );
    }

    public Case createCase(Cases theCase) {
        return caseMap.get(theCase).get();
    }
}



