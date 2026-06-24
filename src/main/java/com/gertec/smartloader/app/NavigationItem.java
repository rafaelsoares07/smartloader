package com.gertec.smartloader.app;

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
