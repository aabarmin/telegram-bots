package dev.abarmin.bots.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.relational.core.mapping.Table;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class ArchUnitModelTest {
    private final JavaClasses importedClasses = new ClassFileImporter()
            .importPackages("dev.abarmin.bots");

    @Test
    @DisplayName("Model classes should be in model package")
    void model_shouldBeInModelPackage() {
        classes().that()
                .areRecords().and()
                .arePublic().and()
                .areNotAnnotatedWith(ConfigurationProperties.class).and()
                .areNotAnnotatedWith(Table.class).and()
                .haveSimpleNameNotEndingWith("Properties")
                .should()
                .resideInAPackage("dev.abarmin.bots.model..")
                .check(importedClasses);
    }
}
