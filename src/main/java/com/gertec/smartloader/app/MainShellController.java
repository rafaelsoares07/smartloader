package com.gertec.smartloader.app;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.Map;


@Component
public class MainShellController {

    private final SpringFxmlLoader fxmlLoader;

    @FXML
    private BorderPane rootPane;
    @FXML
    private Label breadcrumbLabel;

    @FXML
    private Button dashboardButton;
    @FXML
    private Button catalogButton;
    @FXML
    private Button profileButton;
    @FXML
    private Button recordingButton;
    @FXML
    private Button installerButton;

    private final Map<NavigationItem, Button> navButtons = new EnumMap<>(NavigationItem.class);

    public MainShellController(SpringFxmlLoader fxmlLoader) {
        this.fxmlLoader = fxmlLoader;
    }

    @FXML
    private void initialize() {
        navButtons.put(NavigationItem.DASHBOARD, dashboardButton);
        navButtons.put(NavigationItem.CATALOG, catalogButton);
        navButtons.put(NavigationItem.PROFILE, profileButton);
        navButtons.put(NavigationItem.RECORDING, recordingButton);
        navButtons.put(NavigationItem.INSTALLER, installerButton);

        // On open, show the Dashboard (the shell home).
        navigate(NavigationItem.DASHBOARD);
    }

    @FXML
    private void showDashboard() {
        navigate(NavigationItem.DASHBOARD);
    }

    @FXML
    private void showCatalog() {
        navigate(NavigationItem.CATALOG);
    }

    @FXML
    private void showProfile() {
        navigate(NavigationItem.PROFILE);
    }

    @FXML
    private void showRecording() {
        navigate(NavigationItem.RECORDING);
    }

    @FXML
    private void showInstaller() {
        navigate(NavigationItem.INSTALLER);
    }

    private void navigate(NavigationItem item) {
        Parent view = fxmlLoader.load(item.fxml());
        rootPane.setCenter(view);
        breadcrumbLabel.setText(item.breadcrumb());
        highlightActive(item);
    }

    private void highlightActive(NavigationItem active) {
        navButtons.forEach((item, button) -> {
            button.getStyleClass().remove("active");
            if (item == active) {
                button.getStyleClass().add("active");
            }
        });
    }
}
