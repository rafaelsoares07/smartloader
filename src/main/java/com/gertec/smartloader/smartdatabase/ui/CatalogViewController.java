package com.gertec.smartloader.smartdatabase.ui;

import com.gertec.smartloader.smartdatabase.application.CreateApkUseCase;
import com.gertec.smartloader.smartdatabase.application.ListApksUseCase;
import com.gertec.smartloader.smartdatabase.application.RemoveApkUseCase;
import com.gertec.smartloader.smartdatabase.domain.entity.Apk;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;


@Component
@Scope("prototype")
public class CatalogViewController {

    private final CreateApkUseCase createApk;
    private final ListApksUseCase listApks;
    private final RemoveApkUseCase removeApk;

    @FXML private TextField packageNameField;
    @FXML private TextField labelField;
    @FXML private TextField versionNameField;

    @FXML private TableView<Apk> apkTable;
    @FXML private TableColumn<Apk, String> packageColumn;
    @FXML private TableColumn<Apk, String> labelColumn;
    @FXML private TableColumn<Apk, String> versionColumn;

    public CatalogViewController(CreateApkUseCase createApk,
                                 ListApksUseCase listApks,
                                 RemoveApkUseCase removeApk) {
        this.createApk = createApk;
        this.listApks = listApks;
        this.removeApk = removeApk;
    }

    @FXML
    private void initialize() {
        packageColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().packageName()));
        labelColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().label()));
        versionColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().versionName()));
        refreshTable();
    }

    @FXML
    private void onAdd() {
        try {
            createApk.execute(new CreateApkUseCase.Input(
                    packageNameField.getText(),
                    labelField.getText(),
                    versionNameField.getText(),
                    1));
            clearForm();
            refreshTable();
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onRemove() {
        Apk selected = apkTable.getSelectionModel().getSelectedItem();
        if (selected != null) {
            removeApk.execute(selected.id());
            refreshTable();
        }
    }

    private void refreshTable() {apkTable.getItems().setAll(listApks.execute());}

    private void clearForm() {
        packageNameField.clear();
        labelField.clear();
        versionNameField.clear();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Dados inválidos");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}