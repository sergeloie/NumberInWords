package ru.anseranser.cases;

import ru.anseranser.enums.Cases;

public final class CaseFactory {

    private CaseFactory() {
        throw new IllegalStateException("Utility class");
    }

    public static Case createCase(Cases theCase) {
        return switch (theCase) {
            case NOMINATIVE -> new Nominative();
            case GENITIVE -> new Genitive();
            case DATIVE -> new Dative();
            case ACCUSATIVE -> new Accusative();
            case INSTRUMENTAL -> new Instrumental();
            case PREPOSITIONAL -> new Prepositional();
        };
    }
}
