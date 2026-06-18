package com.gertec.smartloader.app;

/**
 * The navigation targets owned by the app shell.
 *
 * <p>Only the {@code app} package knows the FXML locations of the three features; the
 * features never import one another. Each item carries its breadcrumb and the absolute
 * classpath path of the FXML to load into the content area.</p>
 */
public enum NavigationItem {

    DASHBOARD("Dashboard", "HOME / Dashboard", "/fxml/dashboard-view.fxml"),
    CATALOG("Smart Database", "HOME / Smart Database", "/fxml/catalog-view.fxml"),
    PROFILE("Smart Package", "HOME / Smart Package", "/fxml/profile-view.fxml"),
    RECORDING("Smart Hub", "HOME / Smart Hub", "/fxml/recording-view.fxml"),
    INSTALLER("Instalador", "HOME / Instalador", "/fxml/installer-view.fxml");

    private final String label;
    private final String breadcrumb;
    private final String fxml;

    NavigationItem(String label, String breadcrumb, String fxml) {
        this.label = label;
        this.breadcrumb = breadcrumb;
        this.fxml = fxml;
    }

    public String label() {
        return label;
    }

    public String breadcrumb() {
        return breadcrumb;
    }

    public String fxml() {
        return fxml;
    }
}
