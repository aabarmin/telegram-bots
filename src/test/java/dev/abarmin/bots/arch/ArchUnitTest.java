package dev.abarmin.bots.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class ArchUnitTest {
    private final JavaClasses importedClasses = new ClassFileImporter()
            .importPackages("dev.abarmin.bots");

    @Test
    @DisplayName("Arch Unit tests should be in arch package")
    void archTests_shouldBeInArchPackage() {
        classes().that()
                .resideInAPackage("dev.abarmin.bots.arch")
                .should()
                .haveSimpleNameStartingWith("ArchUnit")
                .check(importedClasses);

        classes().that()
                .haveSimpleNameStartingWith("ArchUnit")
                .should()
                .resideInAPackage("dev.abarmin.bots.arch")
                .check(importedClasses);
    }
}
