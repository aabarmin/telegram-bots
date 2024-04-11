package dev.abarmin.bots.arch;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.relational.core.mapping.Table;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

@Disabled
public class ArchUnitEntityTest {
    private final JavaClasses importedClasses = new ClassFileImporter()
            .importPackages("dev.abarmin.bots");

    @Test
    @DisplayName("Entity classes should be in entity package")
    void entity_shouldBeInEntityPackage() {
        classes().that()
                .areAnnotatedWith(Table.class)
                .should()
                .resideInAPackage("dev.abarmin.bots.entity..")
                .check(importedClasses);

        classes().that()
                .resideInAPackage("dev.abarmin.bots.entity..")
                .should()
                .beAnnotatedWith(Table.class)
                .check(importedClasses);
    }
}
