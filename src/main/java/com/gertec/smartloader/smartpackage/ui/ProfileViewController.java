package com.gertec.smartloader.smartpackage.ui;

import com.gertec.smartloader.smartpackage.application.ModuleInfoUseCase;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class ProfileViewController {

    private final ModuleInfoUseCase moduleInfoUseCase;

    @FXML
    private Label messageLabel;

    public ProfileViewController(@Qualifier("profileModuleInfoUseCase") ModuleInfoUseCase moduleInfoUseCase) {
        this.moduleInfoUseCase = moduleInfoUseCase;
    }

    @FXML
    private void initialize() {
        messageLabel.setText(moduleInfoUseCase.message());
    }
}
