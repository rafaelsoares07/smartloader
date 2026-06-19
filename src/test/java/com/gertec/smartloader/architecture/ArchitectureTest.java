package com.gertec.smartloader.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

class ArchitectureTest {

    private static final String ROOT = "com.gertec.smartloader";

    private final JavaClasses classes = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages(ROOT);


    @Test
    void domainMustNotDependOnFrameworks() {
        noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat().resideInAnyPackage("org.springframework..", "javafx..")
                .check(classes);
    }

    @Test
    void applicationMustNotDependOnFrameworks() {
        noClasses()
                .that().resideInAPackage("..application..")
                .should().dependOnClassesThat().resideInAnyPackage("org.springframework..", "javafx..")
                .check(classes);
    }


    @Test
    void catalogLayersMustPointInward() {
        assertInwardLayers(ROOT + ".smartdatabase");
    }

    @Test
    void profileLayersMustPointInward() {
        assertInwardLayers(ROOT + ".smartpackage");
    }

    @Test
    void recordingLayersMustPointInward() {
        assertInwardLayers(ROOT + ".smarthub");
    }

    private void assertInwardLayers(String featureBase) {
        layeredArchitecture().consideringOnlyDependenciesInLayers()
                .layer("Domain").definedBy(featureBase + ".domain..")
                .layer("Application").definedBy(featureBase + ".application..")
                .layer("Infrastructure").definedBy(featureBase + ".infrastructure..")
                .layer("Ui").definedBy(featureBase + ".ui..")
                .whereLayer("Ui").mayNotBeAccessedByAnyLayer()
                .whereLayer("Infrastructure").mayNotBeAccessedByAnyLayer()
                .whereLayer("Application").mayOnlyBeAccessedByLayers("Infrastructure", "Ui")
                .whereLayer("Domain").mayOnlyBeAccessedByLayers("Application", "Infrastructure", "Ui")
                .check(classes);
    }


    @Test
    void featuresMustBeFreeOfCycles() {
        slices().matching(ROOT + ".(*)..")
                .should().beFreeOfCycles()
                .check(classes);
    }

    @Test
    void catalogMustNotDependOnOtherFeaturesOrApp() {
        noClasses()
                .that().resideInAPackage("..smartdatabase..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        ROOT + ".profile..", ROOT + ".recording..", ROOT + ".app..")
                .check(classes);
    }

    @Test
    void profileMustNotDependOnRecordingOrApp() {
        noClasses()
                .that().resideInAPackage("..smartpackage..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        ROOT + ".recording..", ROOT + ".app..")
                .check(classes);
    }

    @Test
    void recordingMustNotDependOnApp() {
        noClasses()
                .that().resideInAPackage("..smarthub..")
                .should().dependOnClassesThat().resideInAPackage(ROOT + ".app..")
                .check(classes);
    }

    @Test
    void sharedMustNotDependOnFeaturesOrApp() {
        noClasses()
                .that().resideInAPackage("..shared..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        ROOT + ".catalog..", ROOT + ".profile..", ROOT + ".recording..", ROOT + ".app..")
                .check(classes);
    }
}
