package dev.abarmin.bots.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.repository.Repository;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class ArchUnitRepositoryTest {
    private final JavaClasses importedClasses = new ClassFileImporter()
            .importPackages("dev.abarmin.bots");

    @Test
    @DisplayName("Repositories should be in repository package")
    void repositories_shouldBeInRepositoryPackage() {
        classes().that()
                .areInterfaces().and()
                .areAssignableTo(Repository.class)
                .should()
                .haveSimpleNameEndingWith("Repository").andShould()
                .resideInAPackage("dev.abarmin.bots.repository")
                .check(importedClasses);
    }
}
