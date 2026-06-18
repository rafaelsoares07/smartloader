package com.gertec.smartloader.catalog.ui;

import com.gertec.smartloader.catalog.application.ModuleInfoUseCase;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * UI controller for the Catalog (Smart Database) view.
 *
 * <p>Spring bean instantiated by the FXMLLoader controller factory. Prototype-scoped so
 * each navigation gets a fresh controller (avoids stale state on a singleton when the
 * FXML is reloaded). The {@link ModuleInfoUseCase} is injected by constructor; the
 * {@code @Qualifier} disambiguates the Catalog use case from the Profile/Recording ones.</p>
 */
@Component
@Scope("prototype")
public class CatalogViewController {

    private final ModuleInfoUseCase moduleInfoUseCase;

    @FXML
    private Label messageLabel;

    public CatalogViewController(@Qualifier("catalogModuleInfoUseCase") ModuleInfoUseCase moduleInfoUseCase) {
        this.moduleInfoUseCase = moduleInfoUseCase;
    }

    @FXML
    private void initialize() {
        messageLabel.setText(moduleInfoUseCase.message());
    }
}
