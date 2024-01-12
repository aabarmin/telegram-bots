package dev.abarmin.bots.arch;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

public class ArchUnitControllerTest {
    private final JavaClasses importedClasses = new ClassFileImporter()
            .importPackages("dev.abarmin.bots");

    @Test
    @DisplayName("Controllers should be in controllers package only")
    void controllers_shouldBeInControllersPackageOnly() {
        classes().that()
                .areAnnotatedWith(Controller.class).or()
                .areAnnotatedWith(RestController.class)
                .should()
                .resideInAPackage("dev.abarmin.bots.controller").andShould()
                .haveSimpleNameEndingWith("Controller")
                .check(importedClasses);

        classes().that()
                .resideInAPackage("dev.abarmin.bots.controller").or()
                .haveSimpleNameEndingWith("Controller").and()
                .haveSimpleNameNotContaining("Test")
                .should()
                .beAnnotatedWith(Controller.class).orShould()
                .beAnnotatedWith(RestController.class)
                .check(importedClasses);
    }

    @Test
    @DisplayName("Controllers should have tests in controllers packages only")
    void controllers_shouldHaveTestsInControllersPackagesOnly() {
        classes().that()
                .haveSimpleNameEndingWith("ControllerTest").and()
                .haveSimpleNameNotContaining("ArchUnit")
                .should()
                .resideInAPackage("dev.abarmin.bots.controller")
                .check(importedClasses);
    }

    @Test
    @Disabled
    @DisplayName("Services should have name ending with Service")
    void services_shouldBeNamedProperly() {
        classes().that()
                .resideInAPackage("dev.abarmin.bots.service").and()
                .areInterfaces()
                .should()
                .haveSimpleNameEndingWith("Service")
                .check(importedClasses);

        classes().that()
                .resideInAPackage("dev.abarmin.bots.service").and()
                .areNotInterfaces().and()
                .areAnnotatedWith(Service.class).and()
                .implement(JavaClass.Predicates.resideInAnyPackage("dev.abarmin.bots.service"))
                .should()
                .haveSimpleNameEndingWith("ServiceImpl").andShould()
                .resideInAPackage("dev.abarmin.bots.service.impl")
                .check(importedClasses);
    }

    @Test
    @Disabled
    @DisplayName("Services should be in service package only")
    void services_shouldBeInServicesPackageOnly() {
        classes().that()
                .areAnnotatedWith(Service.class).or()
                .haveSimpleNameEndingWith("Service")
                .should()
                .resideInAPackage("dev.abarmin.bots.service")
                .check(importedClasses);
    }

    @Test
    @DisplayName("Controllers should have name ending with Controller")
    void controllers_shouldBeNamedProperly() {
        classes().that()
                .areAnnotatedWith(Controller.class).or()
                .areAnnotatedWith(RestController.class)
                .should()
                .haveSimpleNameContaining("Controller")
                .check(importedClasses);
    }



    @Test
    @Disabled
    @DisplayName("Enforce layered architecture")
    void layers_enforced() {
        var rules = layeredArchitecture()
                .consideringAllDependencies()
                .layer("Controllers").definedBy("dev.abarmin.bots.controller")
                .layer("Services").definedBy("dev.abarmin.bots.service")

                .whereLayer("Controllers").mayNotBeAccessedByAnyLayer();

        rules.check(importedClasses);
    }

    @Test
    @DisplayName("Controllers should be annotated with Controller annotation")
    void controllers_shouldHaveCorrectAnnotations() {
        classes().that()
                .resideInAPackage("dev.abarmin.bots.controller").and()
                .haveSimpleNameNotContaining("Test")
                .should()
                .beAnnotatedWith(Controller.class).orShould()
                .beAnnotatedWith(RestController.class)
                .check(importedClasses);
    }
}
