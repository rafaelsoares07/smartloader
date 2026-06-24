package com.gertec.smartloader.smarthub.ui;

import com.gertec.smartloader.smarthub.application.ModuleInfoUseCase;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


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
