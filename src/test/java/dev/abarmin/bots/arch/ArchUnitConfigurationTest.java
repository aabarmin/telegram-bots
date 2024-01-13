package dev.abarmin.bots.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class ArchUnitConfigurationTest {
    private final JavaClasses importedClasses = new ClassFileImporter()
            .importPackages("dev.abarmin.bots");

    @Test
    @DisplayName("Configurations should be in config package only")
    void configuration_shouldBeInConfigPackageOnly() {
        classes().that()
                .resideInAPackage("dev.abarmin.bots.config").and()
                .haveSimpleNameNotEndingWith("Properties")
                .should()
                .beAnnotatedWith(Configuration.class).andShould()
                .haveSimpleNameEndingWith("Configuration")
                .check(importedClasses);

        classes().that()
                .areAnnotatedWith(Configuration.class).or()
                .haveSimpleNameEndingWith("Configuration")
                .should()
                .resideInAPackage("dev.abarmin.bots.config")
                .check(importedClasses);
    }

    @Test
    @DisplayName("Properties should be in config package only")
    void configuration_propertiesShouldBeInConfigPackageOnly() {
        classes().that()
                .areAnnotatedWith(ConfigurationProperties.class).or()
                .haveSimpleNameEndingWith("Properties")
                .should()
                .resideInAPackage("dev.abarmin.bots.config")
                .check(importedClasses);
    }
}
