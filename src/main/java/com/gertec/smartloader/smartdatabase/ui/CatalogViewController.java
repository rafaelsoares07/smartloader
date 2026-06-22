package com.gertec.smartloader.smartdatabase.ui;

import com.gertec.smartloader.smartdatabase.application.AssignSignatureToOdmUseCase;
import com.gertec.smartloader.smartdatabase.application.CreateApkUseCase;
import com.gertec.smartloader.smartdatabase.application.CreateOdmUseCase;
import com.gertec.smartloader.smartdatabase.application.CreateSigningProfileUseCase;
import com.gertec.smartloader.smartdatabase.application.CreateTerminalUseCase;
import com.gertec.smartloader.smartdatabase.application.KeystoreValidator;
import com.gertec.smartloader.smartdatabase.application.ListApksUseCase;
import com.gertec.smartloader.smartdatabase.application.ListOdmUseCase;
import com.gertec.smartloader.smartdatabase.application.ListSigningProfilesUseCase;
import com.gertec.smartloader.smartdatabase.application.ListTerminalUseCase;
import com.gertec.smartloader.smartdatabase.application.RemoveApkUseCase;
import com.gertec.smartloader.smartdatabase.application.RemoveOdmUseCase;
import com.gertec.smartloader.smartdatabase.application.RemoveSigningProfileUseCase;
import com.gertec.smartloader.smartdatabase.application.RemoveTerminalUseCase;
import com.gertec.smartloader.smartdatabase.application.UpdateApkUseCase;
import com.gertec.smartloader.smartdatabase.application.UpdateOdmUseCase;
import com.gertec.smartloader.smartdatabase.application.UpdateSigningProfileUseCase;
import com.gertec.smartloader.smartdatabase.application.UpdateTerminalUseCase;
import com.gertec.smartloader.smartdatabase.application.ValidateSigningProfileUseCase;
import com.gertec.smartloader.smartdatabase.domain.entity.Apk;
import com.gertec.smartloader.smartdatabase.domain.entity.Odm;
import com.gertec.smartloader.smartdatabase.domain.entity.SigningProfile;
import com.gertec.smartloader.smartdatabase.domain.entity.TerminalModel;
import com.gertec.smartloader.smartdatabase.domain.enums.ApkStatus;
import com.gertec.smartloader.smartdatabase.domain.enums.ApkType;
import com.gertec.smartloader.smartdatabase.domain.enums.SigningProfileStatus;
import com.gertec.smartloader.smartdatabase.domain.enums.SigningProfileType;
import com.gertec.smartloader.smartdatabase.domain.enums.TerminalType;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Optional;

/**
 * Coordinates the Smart Database screen (APK + Terminal repositories).
 *
 * <p>This controller only wires widgets and calls use cases — no business rules and no
 * direct persistence access. Filtering of the visible rows is a pure UI concern.</p>
 */
@Component
@Scope("prototype")
public class CatalogViewController {

    private static final String FILTER_ALL_TYPES = "Todos os tipos";

    // --- APK use cases ---
    private final CreateApkUseCase createApk;
    private final ListApksUseCase listApks;
    private final RemoveApkUseCase removeApk;
    private final UpdateApkUseCase updateApk;

    // --- Terminal use cases ---
    private final CreateTerminalUseCase createTerminal;
    private final ListTerminalUseCase listTerminals;
    private final RemoveTerminalUseCase removeTerminal;
    private final UpdateTerminalUseCase updateTerminal;

    // --- Signing profile use cases ---
    private final CreateSigningProfileUseCase createSigningProfile;
    private final ListSigningProfilesUseCase listSigningProfiles;
    private final UpdateSigningProfileUseCase updateSigningProfile;
    private final RemoveSigningProfileUseCase removeSigningProfile;
    private final ValidateSigningProfileUseCase validateSigningProfile;

    // --- ODM use cases ---
    private final CreateOdmUseCase createOdm;
    private final ListOdmUseCase listOdms;
    private final UpdateOdmUseCase updateOdm;
    private final RemoveOdmUseCase removeOdm;
    private final AssignSignatureToOdmUseCase assignSignatureToOdm;

    // --- APK widgets ---
    @FXML private TextField apkSearchField;
    @FXML private ComboBox<String> apkTypeFilter;
    @FXML private TableView<Apk> apkTable;
    @FXML private TableColumn<Apk, String> apkNameColumn;
    @FXML private TableColumn<Apk, String> apkVersionColumn;
    @FXML private TableColumn<Apk, String> apkClientColumn;
    @FXML private TableColumn<Apk, ApkType> apkTypeColumn;
    @FXML private TableColumn<Apk, ApkStatus> apkStatusColumn;
    @FXML private TableColumn<Apk, Void> apkActionColumn;

    // --- Terminal widgets ---
    @FXML private TextField terminalNameField;
    @FXML private ComboBox<TerminalType> terminalTypeCombo;
    @FXML private ComboBox<Odm> terminalOdmCombo;
    @FXML private TextField terminalSearchField;
    @FXML private TableView<TerminalModel> terminalTable;
    @FXML private TableColumn<TerminalModel, String> terminalNameColumn;
    @FXML private TableColumn<TerminalModel, TerminalType> terminalTypeColumn;
    @FXML private TableColumn<TerminalModel, String> terminalOdmColumn;
    @FXML private TableColumn<TerminalModel, Void> terminalActionColumn;

    // --- ODM widgets ---
    @FXML private TextField odmNameField;
    @FXML private TextField odmSearchField;
    @FXML private TableView<Odm> odmTable;
    @FXML private TableColumn<Odm, String> odmNameColumn;
    @FXML private TableColumn<Odm, String> odmSignatureColumn;
    @FXML private TableColumn<Odm, Void> odmActionColumn;

    @FXML private Label apkCountLabel;
    @FXML private Label terminalCountLabel;
    @FXML private Label odmCountLabel;
    @FXML private Label signingCountLabel;

    // Vínculo assinatura → ODM (1:1): feito no próprio card de Assinaturas.
    @FXML private ComboBox<Odm> signingOdmCombo;

    // --- Signing profile widgets ---
    @FXML private TextField signingNameField;
    @FXML private TextField signingAliasField;
    @FXML private Label keystoreFileLabel;
    @FXML private PasswordField keystorePasswordField;
    @FXML private PasswordField keyPasswordField;
    @FXML private ComboBox<SigningProfileType> signingTypeCombo;
    @FXML private ComboBox<SigningProfileStatus> signingStatusCombo;
    @FXML private TextField signingNoteField;
    @FXML private TextField signingSearchField;
    @FXML private ComboBox<String> signingTypeFilter;
    @FXML private TableView<SigningProfile> signingTable;
    @FXML private TableColumn<SigningProfile, String> signingNameColumn;
    @FXML private TableColumn<SigningProfile, String> signingAliasColumn;
    @FXML private TableColumn<SigningProfile, SigningProfileType> signingTypeColumn;
    @FXML private TableColumn<SigningProfile, String> signingFileColumn;
    @FXML private TableColumn<SigningProfile, SigningProfileStatus> signingStatusColumn;
    @FXML private TableColumn<SigningProfile, Void> signingActionColumn;

    // Caminho do keystore escolhido pelo FileChooser (não vai para a tabela em texto puro de senha).
    private String selectedKeystorePath;

    private final ObservableList<Apk> apkMaster = FXCollections.observableArrayList();
    private final ObservableList<TerminalModel> terminalMaster = FXCollections.observableArrayList();
    private final ObservableList<SigningProfile> signingMaster = FXCollections.observableArrayList();
    private final ObservableList<Odm> odmMaster = FXCollections.observableArrayList();

    public CatalogViewController(CreateApkUseCase createApk,
                                 ListApksUseCase listApks,
                                 RemoveApkUseCase removeApk,
                                 UpdateApkUseCase updateApk,
                                 CreateTerminalUseCase createTerminal,
                                 ListTerminalUseCase listTerminals,
                                 RemoveTerminalUseCase removeTerminal,
                                 UpdateTerminalUseCase updateTerminal,
                                 CreateSigningProfileUseCase createSigningProfile,
                                 ListSigningProfilesUseCase listSigningProfiles,
                                 UpdateSigningProfileUseCase updateSigningProfile,
                                 RemoveSigningProfileUseCase removeSigningProfile,
                                 ValidateSigningProfileUseCase validateSigningProfile,
                                 CreateOdmUseCase createOdm,
                                 ListOdmUseCase listOdms,
                                 UpdateOdmUseCase updateOdm,
                                 RemoveOdmUseCase removeOdm,
                                 AssignSignatureToOdmUseCase assignSignatureToOdm) {
        this.createApk = createApk;
        this.listApks = listApks;
        this.removeApk = removeApk;
        this.updateApk = updateApk;
        this.createTerminal = createTerminal;
        this.listTerminals = listTerminals;
        this.removeTerminal = removeTerminal;
        this.updateTerminal = updateTerminal;
        this.createSigningProfile = createSigningProfile;
        this.listSigningProfiles = listSigningProfiles;
        this.updateSigningProfile = updateSigningProfile;
        this.removeSigningProfile = removeSigningProfile;
        this.validateSigningProfile = validateSigningProfile;
        this.createOdm = createOdm;
        this.listOdms = listOdms;
        this.updateOdm = updateOdm;
        this.removeOdm = removeOdm;
        this.assignSignatureToOdm = assignSignatureToOdm;
    }

    @FXML
    private void initialize() {
        configureApkTable();
        configureSigningTable();
        configureOdmTable();
        configureTerminalTable();
        configureSummaryCards();
        refreshApks();
        // Ordem importa para os combos de dependência: assinaturas alimentam as ODMs,
        // e as ODMs alimentam o seletor do terminal.
        refreshSigningProfiles();
        refreshOdms();
        refreshTerminals();
    }

    private void configureSummaryCards() {
        apkCountLabel.textProperty().bind(Bindings.size(apkMaster).asString());
        terminalCountLabel.textProperty().bind(Bindings.size(terminalMaster).asString());
        odmCountLabel.textProperty().bind(Bindings.size(odmMaster).asString());
        signingCountLabel.textProperty().bind(Bindings.size(signingMaster).asString());
    }

    // ===================== APK section =====================

    private void configureApkTable() {
        apkNameColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().apkFileName()));
        apkVersionColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().versionName()));
        apkClientColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().client()));
        apkTypeColumn.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().type()));
        apkStatusColumn.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().status()));

        apkTypeColumn.setCellFactory(col -> badgeCell(type ->
                type == ApkType.GERTEC ? "badge-gertec" : "badge-cliente"));
        apkStatusColumn.setCellFactory(col -> badgeCell(status -> switch (status) {
            case ATIVO -> "badge-ativo";
            case PENDENTE -> "badge-pendente";
            case INATIVO -> "badge-inativo";
        }));
        apkActionColumn.setCellFactory(col -> actionCell(this::showApkDetails));

        // Type filter values: "Todos os tipos" + the enum names.
        apkTypeFilter.getItems().add(FILTER_ALL_TYPES);
        for (ApkType type : ApkType.values()) {
            apkTypeFilter.getItems().add(type.name());
        }
        apkTypeFilter.getSelectionModel().selectFirst();

        FilteredList<Apk> filtered = new FilteredList<>(apkMaster, apk -> true);
        apkSearchField.textProperty().addListener((o, old, val) -> filtered.setPredicate(apkPredicate()));
        apkTypeFilter.valueProperty().addListener((o, old, val) -> filtered.setPredicate(apkPredicate()));
        apkTable.setItems(filtered);
    }

    private java.util.function.Predicate<Apk> apkPredicate() {
        String search = safeLower(apkSearchField.getText());
        String typeFilter = apkTypeFilter.getValue();
        return apk -> {
            boolean matchesType = typeFilter == null
                    || FILTER_ALL_TYPES.equals(typeFilter)
                    || apk.type().name().equals(typeFilter);
            boolean matchesSearch = search.isBlank()
                    || safeLower(apk.apkFileName()).contains(search)
                    || safeLower(apk.client()).contains(search)
                    || safeLower(apk.versionName()).contains(search)
                    || safeLower(apk.packageName()).contains(search)
                    || safeLower(apk.label()).contains(search);
            return matchesType && matchesSearch;
        };
    }

    @FXML
    private void onAddApk() {
        showApkDialog("Adicionar APK", null).ifPresent(form -> {
            try {
                createApk.execute(new CreateApkUseCase.Input(form.apkFileName(), form.packageName(),
                        form.label(), form.versionName(), form.versionCode(), form.client(),
                        form.type(), form.status(), form.cloudPath()));
                refreshApks();
            } catch (IllegalArgumentException e) {
                showError(e.getMessage());
            }
        });
    }

    @FXML
    private void onEditApk() {
        editSelectedApk("Editar APK");
    }

    @FXML
    private void onReplaceApk() {
        // "Substituir" reaproveita o fluxo de edição: troca a referência do APK selecionado.
        editSelectedApk("Substituir APK");
    }

    private void editSelectedApk(String title) {
        Apk selected = apkTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Selecione um APK na tabela.");
            return;
        }
        showApkDialog(title, selected).ifPresent(form -> {
            try {
                updateApk.execute(new UpdateApkUseCase.Input(selected.id(), form.apkFileName(),
                        form.packageName(), form.label(), form.versionName(), form.versionCode(),
                        form.client(), form.type(), form.status(), form.cloudPath()));
                refreshApks();
            } catch (IllegalArgumentException e) {
                showError(e.getMessage());
            }
        });
    }

    @FXML
    private void onRemoveApk() {
        Apk selected = apkTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Selecione um APK na tabela.");
            return;
        }
        removeApk.execute(selected.id());
        refreshApks();
    }

    @FXML
    private void onImportApk() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Selecionar APK");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos APK", "*.apk"));
        // Abre numa pasta navegável (home) — evita o diálogo cair num diretório inválido/vazio.
        chooser.setInitialDirectory(existingDirOrHome(null));

        File file = chooser.showOpenDialog(apkTable.getScene().getWindow());

        if (file == null) {
            new Alert(Alert.AlertType.INFORMATION, "Não escolheu nenhum APK").showAndWait();
            return;   // importante: encerra aqui, senão segue para o código de baixo
        }

        // arquivo válido — segue o fluxo (por enquanto, só confirmação)
        new Alert(Alert.AlertType.INFORMATION, "APK selecionado:\n" + file.getName()).showAndWait();
    }

    private void refreshApks() {
        apkMaster.setAll(listApks.execute());
    }

    private void showApkDetails(Apk apk) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalhes do APK");
        alert.setHeaderText(apk.apkFileName());
        alert.setContentText(
                "Package: " + apk.packageName() + "\n"
                        + "Label: " + apk.label() + "\n"
                        + "Versão: " + apk.versionName() + " (" + apk.versionCode() + ")\n"
                        + "Cliente: " + apk.client() + "\n"
                        + "Tipo: " + apk.type() + "\n"
                        + "Status: " + apk.status() + "\n"
                        + "Nuvem: " + apk.cloudPath());
        alert.showAndWait();
    }

    /** Form fields collected by the APK dialog (UI-only transport). */
    private record ApkForm(String apkFileName, String packageName, String label, String versionName,
                           long versionCode, String client, ApkType type, ApkStatus status,
                           String cloudPath) {}

    private Optional<ApkForm> showApkDialog(String title, Apk base) {
        Dialog<ApkForm> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(null);

        ButtonType saveButton = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);

        TextField apkFileName = new TextField();
        apkFileName.setPromptText("nome do arquivo (ex.: app-1.0.0.apk)");
        TextField packageName = new TextField();
        packageName.setPromptText("package (ex.: com.gertec.app)");
        TextField label = new TextField();
        label.setPromptText("label");
        TextField versionName = new TextField();
        versionName.setPromptText("versão (ex.: 1.0.0)");
        TextField versionCode = new TextField();
        versionCode.setPromptText("versionCode (ex.: 100)");
        TextField client = new TextField();
        client.setPromptText("cliente");
        ComboBox<ApkType> type = new ComboBox<>(FXCollections.observableArrayList(ApkType.values()));
        ComboBox<ApkStatus> status = new ComboBox<>(FXCollections.observableArrayList(ApkStatus.values()));
        TextField cloudPath = new TextField();
        cloudPath.setPromptText("caminho em nuvem / referência");

        if (base != null) {
            apkFileName.setText(base.apkFileName());
            packageName.setText(base.packageName());
            label.setText(base.label());
            versionName.setText(base.versionName());
            versionCode.setText(Long.toString(base.versionCode()));
            client.setText(base.client());
            type.setValue(base.type());
            status.setValue(base.status());
            cloudPath.setText(base.cloudPath());
        } else {
            type.setValue(ApkType.GERTEC);
            status.setValue(ApkStatus.PENDENTE);
        }

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        grid.setPadding(new Insets(16));
        int row = 0;
        grid.addRow(row++, new Label("Nome do APK"), apkFileName);
        grid.addRow(row++, new Label("Package"), packageName);
        grid.addRow(row++, new Label("Label"), label);
        grid.addRow(row++, new Label("Versão"), versionName);
        grid.addRow(row++, new Label("versionCode"), versionCode);
        grid.addRow(row++, new Label("Cliente"), client);
        grid.addRow(row++, new Label("Tipo"), type);
        grid.addRow(row++, new Label("Status"), status);
        grid.addRow(row, new Label("Nuvem"), cloudPath);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {
            if (button != saveButton) {
                return null;
            }
            return new ApkForm(
                    apkFileName.getText(),
                    packageName.getText(),
                    label.getText(),
                    versionName.getText(),
                    parseVersionCode(versionCode.getText()),
                    client.getText(),
                    type.getValue(),
                    status.getValue(),
                    cloudPath.getText());
        });

        return dialog.showAndWait();
    }

    private long parseVersionCode(String raw) {
        if (raw == null || raw.isBlank()) {
            return 0L;
        }
        try {
            return Long.parseLong(raw.trim());
        } catch (NumberFormatException e) {
            return 0L;
        }
    }

    // ===================== Terminal section =====================

    private void configureTerminalTable() {
        terminalTypeCombo.setItems(FXCollections.observableArrayList(TerminalType.values()));

        // O seletor de ODM é alimentado pela lista de ODMs cadastradas (dependência do terminal).
        terminalOdmCombo.setItems(odmMaster);
        setComboDisplay(terminalOdmCombo, Odm::name);

        terminalNameColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().terminalName()));
        terminalTypeColumn.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().terminalType()));
        terminalOdmColumn.setCellValueFactory(c -> new SimpleStringProperty(odmNameFor(c.getValue().odmId())));
        terminalActionColumn.setCellFactory(col -> actionCell(this::showTerminalDetails));

        FilteredList<TerminalModel> filtered = new FilteredList<>(terminalMaster, t -> true);
        terminalSearchField.textProperty().addListener((o, old, val) ->
                filtered.setPredicate(terminalPredicate()));
        terminalTable.setItems(filtered);

        // Selecionar uma linha popula o formulário, facilitando edição.
        terminalTable.getSelectionModel().selectedItemProperty().addListener((o, old, selected) -> {
            if (selected != null) {
                terminalNameField.setText(selected.terminalName());
                terminalTypeCombo.setValue(selected.terminalType());
                terminalOdmCombo.setValue(findOdmById(selected.odmId()));
            }
        });
    }

    private java.util.function.Predicate<TerminalModel> terminalPredicate() {
        String search = safeLower(terminalSearchField.getText());
        return terminal -> search.isBlank()
                || safeLower(terminal.terminalName()).contains(search)
                || safeLower(terminal.terminalType().name()).contains(search);
    }

    @FXML
    private void onAddTerminal() {
        try {
            Odm odm = terminalOdmCombo.getValue();
            if (odm == null) {
                showError("Selecione a ODM à qual o terminal pertence.");
                return;
            }
            createTerminal.execute(new CreateTerminalUseCase.Input(
                    terminalNameField.getText(), terminalTypeCombo.getValue(), odm.id()));
            clearTerminalForm();
            refreshTerminals();
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onEditTerminal() {
        TerminalModel selected = terminalTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Selecione um terminal na tabela.");
            return;
        }
        try {
            Odm odm = terminalOdmCombo.getValue();
            if (odm == null) {
                showError("Selecione a ODM à qual o terminal pertence.");
                return;
            }
            updateTerminal.execute(new UpdateTerminalUseCase.Input(
                    selected.id(), terminalNameField.getText(), terminalTypeCombo.getValue(), odm.id()));
            clearTerminalForm();
            refreshTerminals();
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }
    @FXML
    private void onRemoveTerminal() {
        TerminalModel selected = terminalTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Selecione um terminal na tabela.");
            return;
        }
        removeTerminal.execute(selected.id());
        clearTerminalForm();
        refreshTerminals();
    }

    private void refreshTerminals() {
        terminalMaster.setAll(listTerminals.execute());
    }

    private void clearTerminalForm() {
        terminalNameField.clear();
        terminalTypeCombo.getSelectionModel().clearSelection();
        terminalOdmCombo.getSelectionModel().clearSelection();
        terminalTable.getSelectionModel().clearSelection();
    }

    private void showTerminalDetails(TerminalModel terminal) {
        Odm odm = findOdmById(terminal.odmId());
        String odmName = odm != null ? odm.name() : "—";
        String signatureName = odm != null ? signatureNameFor(odm.signatureId()) : "—";

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalhes do terminal");
        alert.setHeaderText(terminal.terminalName());
        alert.setContentText(
                "Tipo: " + terminal.terminalType() + "\n"
                        + "ODM: " + odmName + "\n"
                        + "Assinatura (via ODM): " + signatureName);
        alert.showAndWait();
    }

    // ===================== Signing profile section =====================

    private void configureSigningTable() {
        signingTypeCombo.setItems(FXCollections.observableArrayList(SigningProfileType.values()));
        signingStatusCombo.setItems(FXCollections.observableArrayList(SigningProfileStatus.values()));

        // Combo de vínculo: a assinatura pode ser ligada (1:1) a uma ODM já cadastrada.
        signingOdmCombo.setItems(odmMaster);
        setComboDisplay(signingOdmCombo, Odm::name);

        signingNameColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().name()));
        signingAliasColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().keyAlias()));
        signingFileColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().keystoreFileName()));
        signingTypeColumn.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().type()));
        signingStatusColumn.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().status()));

        signingTypeColumn.setCellFactory(col -> badgeCell(type -> switch (type) {
            case DEBUG -> "badge-debug";
            case RELEASE -> "badge-release";
            case GERTEC -> "badge-gertec";
            case CLIENTE -> "badge-cliente";
        }));
        signingStatusColumn.setCellFactory(col -> badgeCell(status -> switch (status) {
            case ATIVA -> "badge-active";
            case INATIVA -> "badge-inactive";
            case PENDENTE -> "badge-pending";
            case INVALIDA -> "badge-invalid";
        }));
        signingActionColumn.setCellFactory(col -> actionCell(this::showSigningProfileDetails));

        signingTypeFilter.getItems().add(FILTER_ALL_TYPES);
        for (SigningProfileType type : SigningProfileType.values()) {
            signingTypeFilter.getItems().add(type.name());
        }
        signingTypeFilter.getSelectionModel().selectFirst();

        FilteredList<SigningProfile> filtered = new FilteredList<>(signingMaster, p -> true);
        signingSearchField.textProperty().addListener((o, old, val) -> filtered.setPredicate(signingPredicate()));
        signingTypeFilter.valueProperty().addListener((o, old, val) -> filtered.setPredicate(signingPredicate()));
        signingTable.setItems(filtered);

        // Selecionar uma linha popula o formulário para edição.
        signingTable.getSelectionModel().selectedItemProperty().addListener((o, old, selected) -> {
            if (selected != null) {
                fillSigningProfileFormFromSelection(selected);
            }
        });
    }

    private java.util.function.Predicate<SigningProfile> signingPredicate() {
        String search = safeLower(signingSearchField.getText());
        String typeFilter = signingTypeFilter.getValue();
        return profile -> {
            boolean matchesType = typeFilter == null
                    || FILTER_ALL_TYPES.equals(typeFilter)
                    || profile.type().name().equals(typeFilter);
            boolean matchesSearch = search.isBlank()
                    || safeLower(profile.name()).contains(search)
                    || safeLower(profile.keyAlias()).contains(search)
                    || safeLower(profile.type().name()).contains(search);
            return matchesType && matchesSearch;
        };
    }

    @FXML
    private void onChooseKeystoreFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Selecionar keystore");
        chooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Keystore Android", "*.jks", "*.keystore"));
        // Reabre na pasta do keystore já escolhido (se houver); senão, na home.
        File current = selectedKeystorePath != null ? new File(selectedKeystorePath).getParentFile() : null;
        chooser.setInitialDirectory(existingDirOrHome(current));

        File file = chooser.showOpenDialog(signingTable.getScene().getWindow());
        if (file == null) {
            return;
        }
        selectedKeystorePath = file.getAbsolutePath();
        keystoreFileLabel.setText(file.getName());
    }

    @FXML
    private void onImportJks() {
        // "IMPORTAR JKS" reaproveita o mesmo seletor de arquivo do formulário.
        onChooseKeystoreFile();
    }

    @FXML
    private void onAddSigningProfile() {
        try {
            SigningProfile created = createSigningProfile.execute(new CreateSigningProfileUseCase.Input(
                    signingNameField.getText(), selectedKeystorePath, signingAliasField.getText(),
                    keystorePasswordField.getText(), keyPasswordField.getText(),
                    signingTypeCombo.getValue(), signingStatusCombo.getValue(),
                    signingNoteField.getText()));
            linkSignatureToSelectedOdm(created);
            clearSigningProfileForm();
            refreshSigningProfiles();
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onEditSigningProfile() {
        SigningProfile selected = signingTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Selecione uma assinatura na tabela.");
            return;
        }
        // Se o usuário não escolheu novo arquivo, mantém o caminho já cadastrado.
        String keystorePath = selectedKeystorePath != null ? selectedKeystorePath : selected.keystorePath();
        try {
            updateSigningProfile.execute(new UpdateSigningProfileUseCase.Input(
                    selected.id(), signingNameField.getText(), keystorePath, signingAliasField.getText(),
                    keystorePasswordField.getText(), keyPasswordField.getText(),
                    signingTypeCombo.getValue(), signingStatusCombo.getValue(),
                    signingNoteField.getText()));
            linkSignatureToSelectedOdm(selected);
            clearSigningProfileForm();
            refreshSigningProfiles();
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    // Liga (1:1) a assinatura à ODM escolhida no combo, se houver. Não é obrigatório vincular.
    private void linkSignatureToSelectedOdm(SigningProfile signature) {
        Odm odm = signingOdmCombo.getValue();
        if (odm == null) {
            return;
        }
        assignSignatureToOdm.execute(new AssignSignatureToOdmUseCase.Input(odm.id(), signature.id()));
        refreshOdms();
        refreshTerminals();
    }

    @FXML
    private void onRemoveSigningProfile() {
        SigningProfile selected = signingTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Selecione uma assinatura na tabela.");
            return;
        }
        removeSigningProfile.execute(selected.id());
        clearSigningProfileForm();
        refreshSigningProfiles();
    }

    private void refreshSigningProfiles() {
        signingMaster.setAll(listSigningProfiles.execute());
    }

    private void clearSigningProfileForm() {
        signingNameField.clear();
        signingAliasField.clear();
        keystorePasswordField.clear();
        keyPasswordField.clear();
        signingNoteField.clear();
        signingTypeCombo.getSelectionModel().clearSelection();
        signingStatusCombo.getSelectionModel().clearSelection();
        signingOdmCombo.getSelectionModel().clearSelection();
        keystoreFileLabel.setText("Nenhum arquivo selecionado");
        selectedKeystorePath = null;
        signingTable.getSelectionModel().clearSelection();
    }

    private void fillSigningProfileFormFromSelection(SigningProfile profile) {
        signingNameField.setText(profile.name());
        signingAliasField.setText(profile.keyAlias());
        signingTypeCombo.setValue(profile.type());
        signingStatusCombo.setValue(profile.status());
        signingNoteField.setText(profile.note());
        // Pré-seleciona a ODM que usa esta assinatura (se alguma), para edição do vínculo.
        signingOdmCombo.setValue(findOdmBySignatureId(profile.id()));
        keystoreFileLabel.setText(profile.keystoreFileName());
        selectedKeystorePath = profile.keystorePath();
        // Senhas ficam mascaradas no PasswordField (nunca em texto puro na tela).
        keystorePasswordField.setText(profile.keystorePassword());
        keyPasswordField.setText(profile.keyPassword());
    }

    private void showSigningProfileDetails(SigningProfile profile) {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Detalhes da assinatura");
        dialog.setHeaderText(profile.name());

        // TESTAR CHAVE fica à esquerda; FECHAR encerra o diálogo.
        ButtonType testButton = new ButtonType("TESTAR CHAVE", ButtonBar.ButtonData.LEFT);
        dialog.getDialogPane().getButtonTypes().addAll(testButton, ButtonType.CLOSE);

        // IMPORTANTE: nunca exibir keystorePassword/keyPassword aqui.
        Label info = new Label(
                "Alias: " + profile.keyAlias() + "\n"
                        + "Tipo: " + profile.type() + "\n"
                        + "Status: " + profile.status() + "\n"
                        + "Arquivo: " + profile.keystoreFileName() + "\n"
                        + "Senha do keystore: ********\n"
                        + "Senha da chave: ********\n"
                        + "Observação: " + profile.note() + "\n"
                        + "Criado em: " + profile.createdAt() + "\n"
                        + "Atualizado em: " + profile.updatedAt());

        // Indicador (pin) que aparece só após o teste: verde = válida, vermelho = inválida.
        Label keyStatusBadge = new Label();
        keyStatusBadge.setVisible(false);

        VBox content = new VBox(12, info, keyStatusBadge);
        content.setPadding(new Insets(16));
        dialog.getDialogPane().setContent(content);

        // Consome o evento do botão para o diálogo NÃO fechar ao testar.
        Button testBtn = (Button) dialog.getDialogPane().lookupButton(testButton);
        testBtn.addEventFilter(ActionEvent.ACTION, event -> {
            event.consume();
            KeystoreValidator.Result result = validateSigningProfile.execute(
                    new ValidateSigningProfileUseCase.Input(
                            profile.keystorePath(), profile.keyAlias(),
                            profile.keystorePassword(), profile.keyPassword()));
            // "●" colorido pelo styleClass do badge (verde/vermelho), sem cor hardcoded.
            keyStatusBadge.setText("●  " + (result.valid() ? "Chave válida" : "Chave inválida"));
            keyStatusBadge.getStyleClass().setAll("badge", result.valid() ? "badge-active" : "badge-invalid");
            keyStatusBadge.setVisible(true);
        });

        dialog.showAndWait();
    }

    // ===================== ODM section =====================

    private void configureOdmTable() {
        odmNameColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().name()));
        odmSignatureColumn.setCellValueFactory(c -> new SimpleStringProperty(signatureNameFor(c.getValue().signatureId())));
        odmActionColumn.setCellFactory(col -> actionCell(this::showOdmDetails));

        FilteredList<Odm> filtered = new FilteredList<>(odmMaster, o -> true);
        odmSearchField.textProperty().addListener((o, old, val) -> filtered.setPredicate(odmPredicate()));
        odmTable.setItems(filtered);

        // Selecionar uma linha popula o nome no formulário, facilitando a edição.
        odmTable.getSelectionModel().selectedItemProperty().addListener((o, old, selected) -> {
            if (selected != null) {
                odmNameField.setText(selected.name());
            }
        });
    }

    private java.util.function.Predicate<Odm> odmPredicate() {
        String search = safeLower(odmSearchField.getText());
        return odm -> search.isBlank()
                || safeLower(odm.name()).contains(search)
                || safeLower(signatureNameFor(odm.signatureId())).contains(search);
    }

    @FXML
    private void onAddOdm() {
        try {
            // A ODM nasce só com o nome; a assinatura é vinculada no card de Assinaturas.
            createOdm.execute(new CreateOdmUseCase.Input(odmNameField.getText()));
            clearOdmForm();
            refreshOdms();
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onEditOdm() {
        Odm selected = odmTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Selecione uma ODM na tabela.");
            return;
        }
        try {
            updateOdm.execute(new UpdateOdmUseCase.Input(selected.id(), odmNameField.getText()));
            clearOdmForm();
            refreshOdms();
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void onRemoveOdm() {
        Odm selected = odmTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Selecione uma ODM na tabela.");
            return;
        }
        removeOdm.execute(selected.id());
        clearOdmForm();
        refreshOdms();
    }

    private void refreshOdms() {
        odmMaster.setAll(listOdms.execute());
    }

    private void clearOdmForm() {
        odmNameField.clear();
        odmTable.getSelectionModel().clearSelection();
    }

    private void showOdmDetails(Odm odm) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detalhes da ODM");
        alert.setHeaderText(odm.name());
        alert.setContentText("Assinatura vinculada: " + signatureNameFor(odm.signatureId()));
        alert.showAndWait();
    }

    // Resolve o nome da assinatura a partir do id guardado na ODM (ou "—" quando não há vínculo).
    private String signatureNameFor(String signatureId) {
        SigningProfile profile = findSigningProfileById(signatureId);
        return profile != null ? profile.name() : "—";
    }

    private SigningProfile findSigningProfileById(String id) {
        if (id == null || id.isBlank()) return null;
        return signingMaster.stream().filter(p -> p.id().equals(id)).findFirst().orElse(null);
    }

    private Odm findOdmById(String id) {
        if (id == null || id.isBlank()) return null;
        return odmMaster.stream().filter(o -> o.id().equals(id)).findFirst().orElse(null);
    }

    // Encontra a ODM que usa uma dada assinatura (vínculo 1:1), para pré-selecionar na edição.
    private Odm findOdmBySignatureId(String signatureId) {
        if (signatureId == null || signatureId.isBlank()) return null;
        return odmMaster.stream().filter(o -> signatureId.equals(o.signatureId())).findFirst().orElse(null);
    }

    // Resolve o nome da ODM a partir do id guardado no terminal (ou "—" quando não encontrada).
    private String odmNameFor(String odmId) {
        Odm odm = findOdmById(odmId);
        return odm != null ? odm.name() : "—";
    }

    // ===================== Shared cell helpers =====================

    /** Renders an enum value as a coloured badge; {@code styleResolver} picks the styleClass. */
    private <S, E extends Enum<E>> TableCell<S, E> badgeCell(java.util.function.Function<E, String> styleResolver) {
        return new TableCell<>() {
            @Override
            protected void updateItem(E item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                Label badge = new Label(item.name());
                badge.getStyleClass().setAll("badge", styleResolver.apply(item));
                setGraphic(badge);
                setText(null);
            }
        };
    }

    /** Renders a "VER" link button that runs {@code action} on the row's value. */
    private <S> TableCell<S, Void> actionCell(java.util.function.Consumer<S> action) {
        return new TableCell<>() {
            private final Button button = new Button("VER");
            {
                button.getStyleClass().setAll("action-link");
                button.setOnAction(e -> action.accept(getTableView().getItems().get(getIndex())));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : button);
            }
        };
    }

    private static String safeLower(String value) {
        return value == null ? "" : value.toLowerCase().trim();
    }

    /** Devolve um diretório navegável para o FileChooser: o candidato se existir, senão a home. */
    private static File existingDirOrHome(File candidate) {
        if (candidate != null && candidate.isDirectory()) {
            return candidate;
        }
        return new File(System.getProperty("user.home"));
    }

    /** Exibe entidades no ComboBox pelo texto escolhido (ex.: nome), preservando o objeto como valor. */
    private <T> void setComboDisplay(ComboBox<T> combo, java.util.function.Function<T, String> toText) {
        combo.setConverter(new javafx.util.StringConverter<>() {
            @Override public String toString(T item) { return item == null ? "" : toText.apply(item); }
            @Override public T fromString(String text) { return null; }
        });
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Dados inválidos");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
