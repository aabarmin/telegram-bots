package dev.abarmin.bots.arch;

import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Service;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

public class ArchUnitServiceTest {
    private final JavaClasses importedClasses = new ClassFileImporter()
            .importPackages("dev.abarmin.bots");

    @Test
    @DisplayName("Services should be in service package only")
    void services_shouldBeInServicesPackageOnly() {
        classes().that()
                .areInterfaces().and()
                .haveSimpleNameEndingWith("Service")
                .should()
                .resideInAPackage("dev.abarmin.bots.service").andShould()
                .notBeAnnotatedWith(Service.class)
                .check(importedClasses);
    }

    @Test
    @DisplayName("Services implementations should be in impl package only")
    void services_implementationsShouldBeInImplPackageOnly() {
        classes().that()
                .areNotInterfaces().and()
                .haveSimpleNameEndingWith("ServiceImpl")
                .should()
                .resideInAPackage("dev.abarmin.bots.service.impl").andShould()
                .beAnnotatedWith(Service.class)
                .check(importedClasses);
    }

    @Test
    @DisplayName("Service implementations should be annotated with Service annotation")
    void service_implementationsShouldBeAnnotatedWithServiceAnnotation() {
        classes().that()
                .implement(JavaClass.Predicates.simpleNameEndingWith("Service"))
                .should()
                .beAnnotatedWith(Service.class).andShould()
                .haveSimpleNameEndingWith("ServiceImpl")
                .check(importedClasses);
    }
}
