package dev.abarmin.bots.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.properties.CanBeAnnotated;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class ArchUnitEventListenerTest {
    private final JavaClasses importedClasses = new ClassFileImporter()
            .importPackages("dev.abarmin.bots");

    @Test
    @DisplayName("Event listener should have name ending event listener")
    void eventListener_shouldHaveCorrectName() {
        classes().that()
                .containAnyMethodsThat(CanBeAnnotated.Predicates.annotatedWith(EventListener.class))
                .should()
                .resideInAPackage("dev.abarmin.bots.listener").andShould()
                .haveSimpleNameEndingWith("EventListener").andShould()
                .beAnnotatedWith(Component.class).andShould()
                .haveOnlyFinalFields()
                .check(importedClasses);
    }
}
