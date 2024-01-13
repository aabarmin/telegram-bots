package dev.abarmin.bots.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

public class ArchUnitListenerTest {
    private final JavaClasses importedClasses = new ClassFileImporter()
            .importPackages("dev.abarmin.bots");

    @Test
    @DisplayName("Listener package should have only listeners")
    void listener_shouldHaveOnlyListenerClasses() {
        classes().that()
                .resideInAnyPackage("dev.abarmin.bots.listener..").and()
                .haveSimpleNameNotEndingWith("Test")
                .should()
                .haveSimpleNameEndingWith("Listener").andShould()
                .beAnnotatedWith(Component.class)
                .check(importedClasses);

        classes().that()
                .haveSimpleNameEndingWith("Listener")
                .should()
                .beAnnotatedWith(Component.class).andShould()
                .resideInAPackage("dev.abarmin.bots.listener")
                .check(importedClasses);
    }

    @Test
    @DisplayName("Listeners should not use autowired fields")
    void listener_shouldNotUseAutowired() {
        classes().that()
                .resideInAPackage("dev.abarmin.bots.listener..").and()
                .haveSimpleNameNotEndingWith("Test")
                .should()
                .haveOnlyFinalFields()
                .check(importedClasses);
    }
}
