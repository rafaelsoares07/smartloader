package com.gertec.smartloader.smarthub.ui;

import com.gertec.smartloader.smarthub.application.ModuleInfoUseCase;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

/**
 * UI controller for the Recording (Smart Hub) view. Prototype-scoped Spring bean
 * instantiated by the FXMLLoader controller factory.
 */
@Component
@Scope("prototype")
public class RecordingViewController {

    private final ModuleInfoUseCase moduleInfoUseCase;

    @FXML
    private Label messageLabel;

    public RecordingViewController(@Qualifier("recordingModuleInfoUseCase") ModuleInfoUseCase moduleInfoUseCase) {
        this.moduleInfoUseCase = moduleInfoUseCase;
    }

    @FXML
    private void initialize() {
        messageLabel.setText(moduleInfoUseCase.message());
    }
}
