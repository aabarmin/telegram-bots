package dev.abarmin.bots.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.domain.properties.CanBeAnnotated;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class ArchUnitSchedulerTest {
    private final JavaClasses importedClasses = new ClassFileImporter()
            .importPackages("dev.abarmin.bots");

    @Test
    @Disabled
    @DisplayName("Scheduler should be in scheduler package")
    void scheduler_shouldBeInSchedulerPackage() {
        classes().that()
                .containAnyMethodsThat(CanBeAnnotated.Predicates.annotatedWith(Scheduled.class))
                .should()
                .resideInAPackage("dev.abarmin.bots.scheduler").andShould()
                .beAnnotatedWith(Component.class).andShould()
                .haveOnlyFinalFields()
                .check(importedClasses);
    }
}
