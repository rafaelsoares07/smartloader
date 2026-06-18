package com.gertec.smartloader.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

/**
 * Enforces the Clean Architecture rules now that everything lives in a SINGLE Maven module.
 *
 * <p>With one module there is no classpath barrier between layers or features, so the
 * compiler no longer protects the boundaries — ArchUnit does:</p>
 * <ul>
 *   <li>domain and application stay framework-free (no Spring, no JavaFX);</li>
 *   <li>inside each feature the arrows point inward (ui/infrastructure -> application -> domain);</li>
 *   <li>between features the dependencies are acyclic: recording -> profile -> catalog -> shared,
 *       never the other way around, and no feature depends on {@code app}.</li>
 * </ul>
 */
class ArchitectureTest {

    private static final String ROOT = "com.gertec.smartloader";

    private final JavaClasses classes = new ClassFileImporter()
            .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS)
            .importPackages(ROOT);

    // ===================== Layer purity (all features) =====================

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

    // ===================== Layer direction (per feature) =====================

    @Test
    void catalogLayersMustPointInward() {
        assertInwardLayers(ROOT + ".catalog");
    }

    @Test
    void profileLayersMustPointInward() {
        assertInwardLayers(ROOT + ".profile");
    }

    @Test
    void recordingLayersMustPointInward() {
        assertInwardLayers(ROOT + ".recording");
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

    // ===================== Feature dependency direction (acyclic) =====================

    @Test
    void featuresMustBeFreeOfCycles() {
        slices().matching(ROOT + ".(*)..")
                .should().beFreeOfCycles()
                .check(classes);
    }

    @Test
    void catalogMustNotDependOnOtherFeaturesOrApp() {
        noClasses()
                .that().resideInAPackage("..catalog..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        ROOT + ".profile..", ROOT + ".recording..", ROOT + ".app..")
                .check(classes);
    }

    @Test
    void profileMustNotDependOnRecordingOrApp() {
        noClasses()
                .that().resideInAPackage("..profile..")
                .should().dependOnClassesThat().resideInAnyPackage(
                        ROOT + ".recording..", ROOT + ".app..")
                .check(classes);
    }

    @Test
    void recordingMustNotDependOnApp() {
        noClasses()
                .that().resideInAPackage("..recording..")
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
