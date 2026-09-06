package ru.anseranser;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.library.Architectures;
import org.junit.jupiter.api.Test;

class ArchitectureTest {

    private final JavaClasses classes = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages("ru.anseranser");

    @Test
    void layeredArchitectureShouldBeRespected() {
        Architectures.layeredArchitecture()
                .consideringAllDependencies()
                .optionalLayer("Api")
                .definedBy("..api..")
                .optionalLayer("Service")
                .definedBy("..service..")
                .optionalLayer("Repository")
                .definedBy("..repository..")
                .optionalLayer("Common")
                .definedBy("..common..")
                .optionalLayer("Config")
                .definedBy("..config..")
                .optionalLayer("Bootstrap")
                .definedBy("ru.anseranser")
                // Api may depend on Service, but not the other way around
                .whereLayer("Api")
                .mayNotBeAccessedByAnyLayer()
                .whereLayer("Service")
                .mayOnlyBeAccessedByLayers("Api", "Config", "Bootstrap")
                .whereLayer("Repository")
                .mayOnlyBeAccessedByLayers("Service")
                .check(classes);
    }

    @Test
    void serviceShouldNotDependOnApi() {
        noClasses()
                .that()
                .resideInAPackage("..service..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("..api..")
                .allowEmptyShould(true)
                .check(classes);
    }

    @Test
    void serviceShouldNotDependOnSpringMvc() {
        noClasses()
                .that()
                .resideInAPackage("..service..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("org.springframework.web..")
                .because("service is framework-agnostic, only api may depend on Spring MVC")
                .allowEmptyShould(true)
                .check(classes);
    }

    @Test
    void serviceShouldNotDependOnServletApi() {
        noClasses()
                .that()
                .resideInAPackage("..service..")
                .should()
                .dependOnClassesThat()
                .resideInAPackage("jakarta.servlet..")
                .because("only api layer may depend on Servlet API")
                .allowEmptyShould(true)
                .check(classes);
    }

    @Test
    void commonShouldNotDependOnOtherLayers() {
        noClasses()
                .that()
                .resideInAPackage("..common..")
                .should()
                .dependOnClassesThat()
                .resideInAnyPackage("..api..", "..service..", "..repository..", "..config..")
                .allowEmptyShould(true)
                .check(classes);
    }

    @Test
    void controllersShouldResideInApiPackage() {
        classes()
                .that()
                .haveSimpleNameEndingWith("Controller")
                .should()
                .resideInAPackage("..api..")
                .allowEmptyShould(true)
                .check(classes);
    }

    @Test
    void servicesShouldResideInServicePackage() {
        classes()
                .that()
                .haveSimpleNameEndingWith("Service")
                .should()
                .resideInAPackage("..service..")
                .allowEmptyShould(true)
                .check(classes);
    }

    @Test
    void noCyclesBetweenPackages() {
        // ArchUnit's slices
        com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices()
                .matching("ru.anseranser.(*)..")
                .should()
                .beFreeOfCycles()
                .check(classes);
    }
}
