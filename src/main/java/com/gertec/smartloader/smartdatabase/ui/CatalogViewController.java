package com.gertec.smartloader.smartdatabase.ui;

import com.gertec.smartloader.smartdatabase.application.AnalyzeApkUseCase;
import com.gertec.smartloader.smartdatabase.application.ApkAnalysisException;
import com.gertec.smartloader.smartdatabase.application.ApkAnalyzer;
import com.gertec.smartloader.smartdatabase.application.AssignSignatureToOdmUseCase;
import com.gertec.smartloader.smartdatabase.application.CreateApkUseCase;
import com.gertec.smartloader.smartdatabase.application.CreateClientUseCase;
import com.gertec.smartloader.smartdatabase.application.CreateOdmUseCase;
import com.gertec.smartloader.smartdatabase.application.ListClientUseCase;
import com.gertec.smartloader.smartdatabase.application.CreateSigningProfileUseCase;
import com.gertec.smartloader.smartdatabase.application.CreateTerminalUseCase;
import com.gertec.smartloader.smartdatabase.application.KeystoreValidator;
import com.gertec.smartloader.smartdatabase.application.ListApksUseCase;
import com.gertec.smartloader.smartdatabase.application.ListOdmUseCase;
import com.gertec.smartloader.smartdatabase.application.ListSigningProfilesUseCase;
import com.gertec.smartloader.smartdatabase.application.ListTerminalUseCase;
import com.gertec.smartloader.smartdatabase.application.RemoveApkUseCase;
import com.gertec.smartloader.smartdatabase.application.RemoveClientUseCase;
import com.gertec.smartloader.smartdatabase.application.RemoveOdmUseCase;
import com.gertec.smartloader.smartdatabase.application.SetPrincipalApkUseCase;
import com.gertec.smartloader.smartdatabase.application.RemoveSigningProfileUseCase;
import com.gertec.smartloader.smartdatabase.application.RemoveTerminalUseCase;
import com.gertec.smartloader.smartdatabase.application.UpdateApkUseCase;
import com.gertec.smartloader.smartdatabase.application.UpdateClientUseCase;
import com.gertec.smartloader.smartdatabase.application.UpdateOdmUseCase;
import com.gertec.smartloader.smartdatabase.application.UpdateSigningProfileUseCase;
import com.gertec.smartloader.smartdatabase.application.UpdateTerminalUseCase;
import com.gertec.smartloader.smartdatabase.application.ValidateSigningProfileUseCase;
import com.gertec.smartloader.smartdatabase.domain.entity.Apk;
import com.gertec.smartloader.smartdatabase.domain.entity.Client;
import com.gertec.smartloader.smartdatabase.domain.entity.Odm;
import com.gertec.smartloader.smartdatabase.domain.entity.SigningProfile;
import com.gertec.smartloader.smartdatabase.domain.entity.TerminalModel;
import com.gertec.smartloader.smartdatabase.domain.enums.ApkStatus;
import com.gertec.smartloader.smartdatabase.domain.enums.ApkType;
import com.gertec.smartloader.smartdatabase.domain.enums.SigningProfileStatus;
import com.gertec.smartloader.smartdatabase.domain.enums.SigningProfileType;
import com.gertec.smartloader.smartdatabase.domain.enums.TerminalType;
import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableCell;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.util.Duration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Coordinates the Smart Database screen (APK + Terminal + ODM + Signing repositories).
 *
 * <p>This controller only wires widgets and calls use cases — no business rules and no
 * direct persistence access. Filtering of the visible rows is a pure UI concern.</p>
 *
 * <p>Interaction pattern (uniform across sections): <b>add</b> and <b>edit</b> open a modal
 * dialog; the editing form never lives in the card. Edit/Delete buttons are disabled until a
 * row is selected. Selecting a row has no side effect on the screen.</p>
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
    private final SetPrincipalApkUseCase setPrincipalApk;
    private final AnalyzeApkUseCase analyzeApk;

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

    // --- Client use cases ---
    private final CreateClientUseCase createClient;
    private final ListClientUseCase listClients;
    private final UpdateClientUseCase updateClient;
    private final RemoveClientUseCase removeClient;

    // --- APK widgets (árvore: app-pai → versões-filhas) ---
    @FXML private TextField apkSearchField;
    @FXML private ComboBox<String> apkTypeFilter;
    @FXML private TreeTableView<ApkRow> apkTree;
    @FXML private TreeTableColumn<ApkRow, String> apkTitleColumn;
    @FXML private TreeTableColumn<ApkRow, String> apkSubtitleColumn;
    @FXML private TreeTableColumn<ApkRow, Boolean> apkCurrentColumn;
    @FXML private TreeTableColumn<ApkRow, String> apkClientColumn;
    @FXML private TreeTableColumn<ApkRow, ApkType> apkTypeColumn;
    @FXML private TreeTableColumn<ApkRow, ApkStatus> apkStatusColumn;
    @FXML private TreeTableColumn<ApkRow, Void> apkActionColumn;
    @FXML private Button apkNewVersionButton;
    @FXML private Button apkSetPrincipalButton;
    @FXML private Button apkEditButton;
    @FXML private Button apkDeleteButton;

    // --- Terminal widgets ---
    @FXML private TextField terminalSearchField;
    @FXML private TableView<TerminalModel> terminalTable;
    @FXML private TableColumn<TerminalModel, String> terminalNameColumn;
    @FXML private TableColumn<TerminalModel, TerminalType> terminalTypeColumn;
    @FXML private TableColumn<TerminalModel, String> terminalOdmColumn;
    @FXML private TableColumn<TerminalModel, Void> terminalActionColumn;
    @FXML private Button terminalAddButton;
    @FXML private Button terminalEditButton;
    @FXML private Button terminalDeleteButton;
    @FXML private Label terminalOdmHint;

    // --- ODM widgets ---
    @FXML private TextField odmSearchField;
    @FXML private TableView<Odm> odmTable;
    @FXML private TableColumn<Odm, String> odmNameColumn;
    @FXML private TableColumn<Odm, String> odmSignatureColumn;
    @FXML private TableColumn<Odm, Void> odmActionColumn;
    @FXML private Button odmEditButton;
    @FXML private Button odmDeleteButton;

    // --- Client widgets ---
    @FXML private TextField clientSearchField;
    @FXML private TreeTableView<ClientRow> clientTree;
    @FXML private TreeTableColumn<ClientRow, String> clientTitleColumn;
    @FXML private TreeTableColumn<ClientRow, String> clientSubtitleColumn;
    @FXML private TreeTableColumn<ClientRow, Boolean> clientCurrentColumn;
    @FXML private TreeTableColumn<ClientRow, ApkType> clientTypeColumn;
    @FXML private TreeTableColumn<ClientRow, ApkStatus> clientStatusColumn;
    @FXML private TreeTableColumn<ClientRow, Void> clientActionColumn;
    @FXML private Button clientSetPrincipalButton;
    @FXML private Button clientEditButton;
    @FXML private Button clientDeleteButton;

    @FXML private Label apkCountLabel;
    @FXML private Label terminalCountLabel;
    @FXML private Label odmCountLabel;
    @FXML private Label signingCountLabel;
    @FXML private Label clientCountLabel;

    // Faixa de feedback transitório (sucesso ao criar/editar/excluir).
    @FXML private Label statusLabel;

    // --- Signing profile widgets ---
    @FXML private TextField signingSearchField;
    @FXML private ComboBox<String> signingTypeFilter;
    @FXML private TableView<SigningProfile> signingTable;
    @FXML private TableColumn<SigningProfile, String> signingNameColumn;
    @FXML private TableColumn<SigningProfile, String> signingAliasColumn;
    @FXML private TableColumn<SigningProfile, SigningProfileType> signingTypeColumn;
    @FXML private TableColumn<SigningProfile, String> signingFileColumn;
    @FXML private TableColumn<SigningProfile, SigningProfileStatus> signingStatusColumn;
    @FXML private TableColumn<SigningProfile, Void> signingActionColumn;
    @FXML private Button signingEditButton;
    @FXML private Button signingDeleteButton;

    private final ObservableList<Apk> apkMaster = FXCollections.observableArrayList();
    private final ObservableList<TerminalModel> terminalMaster = FXCollections.observableArrayList();
    private final ObservableList<SigningProfile> signingMaster = FXCollections.observableArrayList();
    private final ObservableList<Odm> odmMaster = FXCollections.observableArrayList();
    private final ObservableList<Client> clientMaster = FXCollections.observableArrayList();

    public CatalogViewController(CreateApkUseCase createApk,
                                 ListApksUseCase listApks,
                                 RemoveApkUseCase removeApk,
                                 UpdateApkUseCase updateApk,
                                 SetPrincipalApkUseCase setPrincipalApk,
                                 AnalyzeApkUseCase analyzeApk,
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
                                 AssignSignatureToOdmUseCase assignSignatureToOdm,
                                 CreateClientUseCase createClient,
                                 ListClientUseCase listClients,
                                 UpdateClientUseCase updateClient,
                                 RemoveClientUseCase removeClient) {
        this.createApk = createApk;
        this.listApks = listApks;
        this.removeApk = removeApk;
        this.updateApk = updateApk;
        this.setPrincipalApk = setPrincipalApk;
        this.analyzeApk = analyzeApk;
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
        this.createClient = createClient;
        this.listClients = listClients;
        this.updateClient = updateClient;
        this.removeClient = removeClient;
    }

    @FXML
    private void initialize() {
        configureApkTable();
        configureSigningTable();
        configureOdmTable();
        configureTerminalTable();
        configureClientTable();
        configureSummaryCards();
        configureActionButtonStates();
        // Clientes alimentam o seletor do diálogo de APK; carregados antes dos APKs.
        refreshClients();
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
        clientCountLabel.textProperty().bind(Bindings.size(clientMaster).asString());
    }

    /**
     * Habilita Editar/Excluir só quando há linha selecionada e bloqueia "Adicionar terminal"
     * enquanto não existir nenhuma ODM (dependência: todo terminal pertence a uma ODM).
     */
    private void configureActionButtonStates() {
        bindToSelection(odmEditButton, odmTable);
        bindToSelection(odmDeleteButton, odmTable);
        bindToSelection(terminalEditButton, terminalTable);
        bindToSelection(terminalDeleteButton, terminalTable);
        bindToSelection(signingEditButton, signingTable);
        bindToSelection(signingDeleteButton, signingTable);
        // Clientes também são uma árvore (cliente → app → versões):
        // Editar/Excluir agem sobre o cliente; "Tornar principal" sobre uma versão não-principal.
        var clientSelection = clientTree.getSelectionModel().selectedItemProperty();
        clientEditButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> selectedClient() == null, clientSelection));
        clientDeleteButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> selectedClient() == null, clientSelection));
        clientSetPrincipalButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> selectedClientApk() == null || selectedClientApk().principal(), clientSelection));

        // APK é uma árvore: "Nova versão" vale para app (grupo) ou versão selecionada;
        // Editar/Excluir só fazem sentido sobre uma versão (linha-folha).
        var apkSelection = apkTree.getSelectionModel().selectedItemProperty();
        apkNewVersionButton.disableProperty().bind(apkSelection.isNull());
        apkEditButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> selectedApk() == null, apkSelection));
        apkDeleteButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> selectedApk() == null, apkSelection));
        // "Tornar principal" só faz sentido sobre uma versão que ainda NÃO é a principal.
        apkSetPrincipalButton.disableProperty().bind(Bindings.createBooleanBinding(
                () -> selectedApk() == null || selectedApk().principal(), apkSelection));

        // Dependência: sem ODM não há como adicionar terminal.
        terminalAddButton.disableProperty().bind(Bindings.isEmpty(odmMaster));
        terminalOdmHint.visibleProperty().bind(Bindings.isEmpty(odmMaster));
        terminalOdmHint.managedProperty().bind(terminalOdmHint.visibleProperty());
    }

    private void bindToSelection(Button button, TableView<?> table) {
        button.disableProperty().bind(table.getSelectionModel().selectedItemProperty().isNull());
    }

    // ===================== APK section =====================

    /**
     * Linha da árvore de APKs. Pode ser um GRUPO (um app/pacote) ou uma VERSÃO (um Apk real).
     * Para o grupo, {@code apk} aponta para a versão mais recente — assim "Nova versão" a partir
     * do grupo herda os dados certos. {@code current} marca a versão vigente (maior versionCode).
     */
    private record ApkRow(boolean group, String title, String subtitle, String client,
                          ApkType type, ApkStatus status, boolean current, Apk apk) {}

    private void configureApkTable() {
        apkTitleColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getValue().title()));
        apkSubtitleColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getValue().subtitle()));
        apkClientColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getValue().client()));
        apkTypeColumn.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getValue().type()));
        apkStatusColumn.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getValue().status()));
        apkCurrentColumn.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getValue().current()));

        apkTypeColumn.setCellFactory(col -> treeBadgeCell(type ->
                type == ApkType.GERTEC ? "badge-gertec" : "badge-cliente"));
        apkStatusColumn.setCellFactory(col -> treeBadgeCell(status -> switch (status) {
            case ATIVO -> "badge-ativo";
            case PENDENTE -> "badge-pendente";
            case INATIVO -> "badge-inativo";
        }));
        apkCurrentColumn.setCellFactory(col -> currentBadgeCell());
        apkActionColumn.setCellFactory(col -> treeActionCell(
                row -> !row.group() && row.apk() != null,
                row -> showApkDetails(row.apk())));

        // Type filter values: "Todos os tipos" + the enum names.
        apkTypeFilter.getItems().add(FILTER_ALL_TYPES);
        for (ApkType type : ApkType.values()) {
            apkTypeFilter.getItems().add(type.name());
        }
        apkTypeFilter.getSelectionModel().selectFirst();

        apkTree.setShowRoot(false);
        apkTree.setRoot(new TreeItem<>(new ApkRow(true, "", "", "", null, null, false, null)));

        // Busca e filtro reconstroem a árvore aplicando o predicado já existente.
        apkSearchField.textProperty().addListener((o, old, val) -> rebuildApkTree());
        apkTypeFilter.valueProperty().addListener((o, old, val) -> rebuildApkTree());
    }

    /**
     * (Re)constrói a árvore agrupando os APKs por packageName: cada pacote é um app-pai e suas
     * versões são as folhas (ordenadas da mais nova para a mais antiga). Respeita busca/filtro.
     */
    private void rebuildApkTree() {
        java.util.function.Predicate<Apk> predicate = apkPredicate();

        // Agrupa por pacote preservando ordem de inserção dos grupos.
        Map<String, List<Apk>> byPackage = new LinkedHashMap<>();
        for (Apk apk : apkMaster) {
            if (predicate.test(apk)) {
                byPackage.computeIfAbsent(apk.packageName(), k -> new ArrayList<>()).add(apk);
            }
        }

        List<TreeItem<ApkRow>> groups = new ArrayList<>();
        for (Map.Entry<String, List<Apk>> entry : byPackage.entrySet()) {
            List<Apk> versions = entry.getValue();
            // Mais nova primeiro; a de maior versionCode é a "atual".
            versions.sort(Comparator.comparingLong(Apk::versionCode).reversed());
            Apk latest = versions.get(0);

            String countText = versions.size() == 1 ? "1 versão" : versions.size() + " versões";
            ApkRow groupRow = new ApkRow(true, latest.label(),
                    entry.getKey() + "  ·  " + countText, "", null, null, false, latest);
            TreeItem<ApkRow> groupItem = new TreeItem<>(groupRow);
            groupItem.setExpanded(true);

            for (Apk version : versions) {
                // O selo "ATUAL" segue o flag principal (escolha manual), não o versionCode.
                ApkRow leaf = new ApkRow(false, "v" + version.versionName(), version.apkFileName(),
                        version.client(), version.type(), version.status(),
                        version.principal(), version);
                groupItem.getChildren().add(new TreeItem<>(leaf));
            }
            groups.add(groupItem);
        }

        // Ordena os apps pelo nome exibido (case-insensitive).
        groups.sort(Comparator.comparing(g -> safeLower(g.getValue().title())));
        apkTree.getRoot().getChildren().setAll(groups);
    }

    /** APK selecionado na árvore, ou null se nada/um grupo estiver selecionado. */
    private Apk selectedApk() {
        TreeItem<ApkRow> item = apkTree.getSelectionModel().getSelectedItem();
        return item != null && !item.getValue().group() ? item.getValue().apk() : null;
    }

    /** Linha selecionada (grupo ou versão), ou null. Usada pelo fluxo de "Nova versão". */
    private ApkRow selectedApkRow() {
        TreeItem<ApkRow> item = apkTree.getSelectionModel().getSelectedItem();
        return item != null ? item.getValue() : null;
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
        // Fluxo "arquivo primeiro": escolhe o .apk e os campos vêm da análise do próprio arquivo.
        File file = chooseApkFile();
        if (file == null) {
            return;
        }
        ApkAnalyzer.ApkMetadata meta = analyzeApkFile(file);
        if (meta == null) {
            return; // erro já exibido ao usuário
        }
        ApkForm prefill = new ApkForm(meta.fileName(), meta.packageName(), meta.label(),
                meta.versionName(), meta.versionCode(), "", ApkType.GERTEC, ApkStatus.PENDENTE,
                file.getAbsolutePath(), false);
        showApkDialog("Adicionar APK", prefill, true).ifPresent(form -> {
            try {
                Apk created = createApk.execute(new CreateApkUseCase.Input(form.apkFileName(),
                        form.packageName(), form.label(), form.versionName(), form.versionCode(),
                        form.client(), form.type(), form.status(), form.cloudPath()));
                if (form.principal()) {
                    setPrincipalApk.execute(created.id());
                }
                refreshApks();
                flashStatus("APK adicionado.");
            } catch (IllegalArgumentException e) {
                showError(e.getMessage());
            }
        });
    }

    @FXML
    private void onNewApkVersion() {
        // Nova versão de um APK existente: cadastra uma NOVA linha vinculada ao mesmo pacote
        // (histórico preservado). Herda cliente/tipo/label; versão e arquivo vêm do novo .apk.
        // Funciona a partir do app (grupo, usa a versão mais recente) ou de uma versão específica.
        ApkRow row = selectedApkRow();
        if (row == null || row.apk() == null) {
            showError("Selecione na árvore o app ou a versão da qual quer cadastrar uma nova versão.");
            return;
        }
        Apk selected = row.apk();
        File file = chooseApkFile();
        if (file == null) {
            return;
        }
        ApkAnalyzer.ApkMetadata meta = analyzeApkFile(file);
        if (meta == null) {
            return;
        }
        // O vínculo entre versões é o packageName. Se o arquivo for de outro pacote, confirma.
        if (!selected.packageName().equalsIgnoreCase(meta.packageName())
                && !confirmAction("O arquivo escolhido é do pacote \"" + meta.packageName()
                + "\", diferente do APK selecionado (\"" + selected.packageName()
                + "\"). Cadastrar mesmo assim como nova versão?")) {
            return;
        }
        ApkForm prefill = new ApkForm(meta.fileName(), selected.packageName(), selected.label(),
                meta.versionName(), meta.versionCode(), selected.client(), selected.type(),
                ApkStatus.PENDENTE, file.getAbsolutePath(), false);
        showApkDialog("Nova versão · " + selected.label(), prefill, true).ifPresent(form -> {
            try {
                Apk created = createApk.execute(new CreateApkUseCase.Input(form.apkFileName(),
                        form.packageName(), form.label(), form.versionName(), form.versionCode(),
                        form.client(), form.type(), form.status(), form.cloudPath()));
                if (form.principal()) {
                    setPrincipalApk.execute(created.id());
                }
                refreshApks();
                flashStatus("Nova versão (" + form.versionName() + ") adicionada"
                        + (form.principal() ? " e definida como principal." : "."));
            } catch (IllegalArgumentException e) {
                showError(e.getMessage());
            }
        });
    }

    @FXML
    private void onSetPrincipalApk() {
        Apk selected = selectedApk();
        if (selected == null) {
            showError("Selecione uma versão (linha-filha) para tornar principal.");
            return;
        }
        setPrincipalApk.execute(selected.id());
        refreshApks();
        flashStatus("Versão v" + selected.versionName() + " definida como principal.");
    }

    @FXML
    private void onEditApk() {
        Apk selected = selectedApk();
        if (selected == null) {
            showError("Selecione uma versão (linha-filha) para editar.");
            return;
        }
        showApkDialog("Editar APK", formOf(selected), false).ifPresent(form -> {
            try {
                updateApk.execute(new UpdateApkUseCase.Input(selected.id(), form.apkFileName(),
                        form.packageName(), form.label(), form.versionName(), form.versionCode(),
                        form.client(), form.type(), form.status(), form.cloudPath()));
                refreshApks();
                flashStatus("APK atualizado.");
            } catch (IllegalArgumentException e) {
                showError(e.getMessage());
            }
        });
    }

    @FXML
    private void onRemoveApk() {
        Apk selected = selectedApk();
        if (selected == null) {
            showError("Selecione uma versão (linha-filha) para excluir.");
            return;
        }
        if (!confirmDelete("Excluir o APK \"" + selected.apkFileName() + "\"?")) {
            return;
        }
        removeApk.execute(selected.id());
        refreshApks();
        flashStatus("APK excluído.");
    }

    /** Abre o seletor de arquivos .apk; devolve null se o usuário cancelar. */
    private File chooseApkFile() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Selecionar APK");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Arquivos APK", "*.apk"));
        chooser.setInitialDirectory(existingDirOrHome(null));
        return chooser.showOpenDialog(apkTree.getScene().getWindow());
    }

    /** Analisa o .apk; em caso de falha mostra um aviso e devolve null. */
    private ApkAnalyzer.ApkMetadata analyzeApkFile(File file) {
        try {
            return analyzeApk.execute(file.getAbsolutePath());
        } catch (ApkAnalysisException e) {
            showError(e.getMessage());
            return null;
        }
    }

    private void refreshApks() {
        apkMaster.setAll(listApks.execute());
        rebuildApkTree();
        rebuildClientTree(); // a árvore de clientes deriva dos APKs (apps vinculados)
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
                        + "Versão principal: " + (apk.principal() ? "Sim" : "Não") + "\n"
                        + "Nuvem: " + apk.cloudPath());
        alert.showAndWait();
    }

    /** Form fields collected by the APK dialog (UI-only transport). */
    private record ApkForm(String apkFileName, String packageName, String label, String versionName,
                           long versionCode, String client, ApkType type, ApkStatus status,
                           String cloudPath, boolean principal) {}

    /** Converte um Apk persistido em ApkForm para pré-preencher o diálogo de edição. */
    private ApkForm formOf(Apk apk) {
        return new ApkForm(apk.apkFileName(), apk.packageName(), apk.label(), apk.versionName(),
                apk.versionCode(), apk.client(), apk.type(), apk.status(), apk.cloudPath(),
                apk.principal());
    }

    /**
     * @param showPrincipalOption quando true, mostra a opção "Definir como versão principal"
     *        (usado em adicionar/nova versão; na edição o principal é preservado).
     */
    private Optional<ApkForm> showApkDialog(String title, ApkForm prefill, boolean showPrincipalOption) {
        Dialog<ApkForm> dialog = newFormDialog(title);
        ButtonType saveButton = addSaveButton(dialog);

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
        // Origem do APK: "Padrão Gertec" (sem cliente) ou um cliente cadastrado.
        // A escolha define o type (GERTEC/CLIENTE) e o nome do cliente gravado no APK.
        ComboBox<ClientChoice> origin = new ComboBox<>();
        origin.setMaxWidth(Double.MAX_VALUE);
        origin.getItems().add(new ClientChoice(null)); // "Padrão Gertec"
        for (Client c : clientMaster) {
            origin.getItems().add(new ClientChoice(c));
        }
        setComboDisplay(origin, ClientChoice::display);
        ComboBox<ApkStatus> status = new ComboBox<>(FXCollections.observableArrayList(ApkStatus.values()));
        TextField cloudPath = new TextField();
        cloudPath.setPromptText("caminho em nuvem / referência");
        CheckBox principalCheck = new CheckBox("Definir como versão principal (atual) deste app");
        principalCheck.setSelected(prefill != null && prefill.principal());

        if (prefill != null) {
            apkFileName.setText(prefill.apkFileName());
            packageName.setText(prefill.packageName());
            label.setText(prefill.label());
            versionName.setText(prefill.versionName());
            versionCode.setText(Long.toString(prefill.versionCode()));
            origin.setValue(originChoiceFor(origin.getItems(), prefill));
            status.setValue(prefill.status());
            cloudPath.setText(prefill.cloudPath());
        } else {
            origin.setValue(origin.getItems().get(0)); // padrão Gertec
            status.setValue(ApkStatus.PENDENTE);
        }

        GridPane grid = formGrid();
        int row = 0;
        grid.addRow(row++, new Label("Nome do APK"), apkFileName);
        grid.addRow(row++, new Label("Package"), packageName);
        grid.addRow(row++, new Label("Label"), label);
        grid.addRow(row++, new Label("Versão"), versionName);
        grid.addRow(row++, new Label("versionCode"), versionCode);
        grid.addRow(row++, new Label("Cliente / Origem"), origin);
        grid.addRow(row++, new Label("Status"), status);
        grid.addRow(row++, new Label("Nuvem"), cloudPath);
        if (showPrincipalOption) {
            grid.add(principalCheck, 1, row);
        }
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {
            if (button != saveButton) {
                return null;
            }
            ClientChoice choice = origin.getValue();
            boolean gertec = choice == null || choice.isGertec();
            return new ApkForm(
                    apkFileName.getText(),
                    packageName.getText(),
                    label.getText(),
                    versionName.getText(),
                    parseVersionCode(versionCode.getText()),
                    gertec ? "" : choice.client().name(),
                    gertec ? ApkType.GERTEC : ApkType.CLIENTE,
                    status.getValue(),
                    cloudPath.getText(),
                    showPrincipalOption ? principalCheck.isSelected() : (prefill != null && prefill.principal()));
        });

        return dialog.showAndWait();
    }

    /** UI-only: origem do APK no diálogo — "Padrão Gertec" (client null) ou um cliente cadastrado. */
    private record ClientChoice(Client client) {
        boolean isGertec() { return client == null; }
        String display() { return isGertec() ? "Padrão Gertec" : client.name(); }
    }

    /** Pré-seleciona a origem do APK ao editar: o cliente gravado (se houver) ou "Padrão Gertec". */
    private ClientChoice originChoiceFor(List<ClientChoice> items, ApkForm prefill) {
        if (prefill != null && prefill.type() == ApkType.CLIENTE) {
            for (ClientChoice choice : items) {
                if (!choice.isGertec() && choice.client().name().equalsIgnoreCase(prefill.client())) {
                    return choice;
                }
            }
        }
        return items.get(0); // "Padrão Gertec"
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
        terminalNameColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().terminalName()));
        terminalTypeColumn.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().terminalType()));
        terminalOdmColumn.setCellValueFactory(c -> new SimpleStringProperty(odmNameFor(c.getValue().odmId())));
        terminalActionColumn.setCellFactory(col -> actionCell(this::showTerminalDetails));

        FilteredList<TerminalModel> filtered = new FilteredList<>(terminalMaster, t -> true);
        terminalSearchField.textProperty().addListener((o, old, val) ->
                filtered.setPredicate(terminalPredicate()));
        terminalTable.setItems(filtered);
    }

    private java.util.function.Predicate<TerminalModel> terminalPredicate() {
        String search = safeLower(terminalSearchField.getText());
        return terminal -> search.isBlank()
                || safeLower(terminal.terminalName()).contains(search)
                || safeLower(terminal.terminalType().name()).contains(search);
    }

    @FXML
    private void onAddTerminal() {
        showTerminalDialog("Adicionar terminal", null).ifPresent(form -> {
            try {
                createTerminal.execute(new CreateTerminalUseCase.Input(
                        form.name(), form.type(), form.odm().id()));
                refreshTerminals();
                flashStatus("Terminal adicionado.");
            } catch (IllegalArgumentException e) {
                showError(e.getMessage());
            }
        });
    }

    @FXML
    private void onEditTerminal() {
        TerminalModel selected = terminalTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Selecione um terminal na tabela.");
            return;
        }
        showTerminalDialog("Editar terminal", selected).ifPresent(form -> {
            try {
                updateTerminal.execute(new UpdateTerminalUseCase.Input(
                        selected.id(), form.name(), form.type(), form.odm().id()));
                refreshTerminals();
                flashStatus("Terminal atualizado.");
            } catch (IllegalArgumentException e) {
                showError(e.getMessage());
            }
        });
    }

    @FXML
    private void onRemoveTerminal() {
        TerminalModel selected = terminalTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Selecione um terminal na tabela.");
            return;
        }
        if (!confirmDelete("Excluir o terminal \"" + selected.terminalName() + "\"?")) {
            return;
        }
        removeTerminal.execute(selected.id());
        refreshTerminals();
        flashStatus("Terminal excluído.");
    }

    private void refreshTerminals() {
        terminalMaster.setAll(listTerminals.execute());
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

    /** Form fields collected by the terminal dialog (UI-only transport). */
    private record TerminalForm(String name, TerminalType type, Odm odm) {}

    private Optional<TerminalForm> showTerminalDialog(String title, TerminalModel base) {
        Dialog<TerminalForm> dialog = newFormDialog(title);
        ButtonType saveButton = addSaveButton(dialog);

        TextField name = new TextField();
        name.setPromptText("Nome do terminal");
        ComboBox<TerminalType> type = new ComboBox<>(FXCollections.observableArrayList(TerminalType.values()));
        type.setMaxWidth(Double.MAX_VALUE);
        ComboBox<Odm> odm = new ComboBox<>(odmMaster);
        odm.setMaxWidth(Double.MAX_VALUE);
        odm.setPromptText("Selecione a ODM");
        setComboDisplay(odm, Odm::name);

        if (base != null) {
            name.setText(base.terminalName());
            type.setValue(base.terminalType());
            odm.setValue(findOdmById(base.odmId()));
        }

        GridPane grid = formGrid();
        grid.addRow(0, new Label("Nome do terminal"), name);
        grid.addRow(1, new Label("Tipo"), type);
        grid.addRow(2, new Label("ODM"), odm);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {
            if (button != saveButton) {
                return null;
            }
            if (odm.getValue() == null) {
                showError("Selecione a ODM à qual o terminal pertence.");
                return null;
            }
            return new TerminalForm(name.getText(), type.getValue(), odm.getValue());
        });

        return dialog.showAndWait();
    }

    // ===================== Signing profile section =====================

    private void configureSigningTable() {
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
    private void onAddSigningProfile() {
        showSigningDialog("Adicionar assinatura", null).ifPresent(form -> {
            try {
                SigningProfile created = createSigningProfile.execute(new CreateSigningProfileUseCase.Input(
                        form.name(), form.keystorePath(), form.alias(),
                        form.keystorePassword(), form.keyPassword(),
                        form.type(), form.status(), form.note()));
                linkSignatureToOdm(created, form.linkedOdm());
                refreshSigningProfiles();
                flashStatus("Assinatura adicionada.");
            } catch (IllegalArgumentException e) {
                showError(e.getMessage());
            }
        });
    }

    @FXML
    private void onEditSigningProfile() {
        SigningProfile selected = signingTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Selecione uma assinatura na tabela.");
            return;
        }
        showSigningDialog("Editar assinatura", selected).ifPresent(form -> {
            try {
                updateSigningProfile.execute(new UpdateSigningProfileUseCase.Input(
                        selected.id(), form.name(), form.keystorePath(), form.alias(),
                        form.keystorePassword(), form.keyPassword(),
                        form.type(), form.status(), form.note()));
                linkSignatureToOdm(selected, form.linkedOdm());
                refreshSigningProfiles();
                flashStatus("Assinatura atualizada.");
            } catch (IllegalArgumentException e) {
                showError(e.getMessage());
            }
        });
    }

    // Liga (1:1) a assinatura à ODM escolhida no diálogo, se houver. Não é obrigatório vincular.
    private void linkSignatureToOdm(SigningProfile signature, Odm odm) {
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
        if (!confirmDelete("Excluir a assinatura \"" + selected.name() + "\"?")) {
            return;
        }
        removeSigningProfile.execute(selected.id());
        refreshSigningProfiles();
        flashStatus("Assinatura excluída.");
    }

    private void refreshSigningProfiles() {
        signingMaster.setAll(listSigningProfiles.execute());
    }

    /** Form fields collected by the signing dialog (UI-only transport). */
    private record SigningForm(String name, String alias, String keystorePath,
                               String keystorePassword, String keyPassword,
                               SigningProfileType type, SigningProfileStatus status,
                               String note, Odm linkedOdm) {}

    private Optional<SigningForm> showSigningDialog(String title, SigningProfile base) {
        Dialog<SigningForm> dialog = newFormDialog(title);
        ButtonType saveButton = addSaveButton(dialog);

        TextField name = new TextField();
        name.setPromptText("Nome da assinatura");
        TextField alias = new TextField();
        alias.setPromptText("Alias da chave");
        PasswordField keystorePassword = new PasswordField();
        keystorePassword.setPromptText("Senha do keystore");
        PasswordField keyPassword = new PasswordField();
        keyPassword.setPromptText("Senha da chave");
        ComboBox<SigningProfileType> type = new ComboBox<>(FXCollections.observableArrayList(SigningProfileType.values()));
        type.setMaxWidth(Double.MAX_VALUE);
        ComboBox<SigningProfileStatus> status = new ComboBox<>(FXCollections.observableArrayList(SigningProfileStatus.values()));
        status.setMaxWidth(Double.MAX_VALUE);
        ComboBox<Odm> linkedOdm = new ComboBox<>(odmMaster);
        linkedOdm.setMaxWidth(Double.MAX_VALUE);
        linkedOdm.setPromptText("Nenhuma (vincular depois)");
        setComboDisplay(linkedOdm, Odm::name);
        TextField note = new TextField();
        note.setPromptText("Observação");

        // Seletor de arquivo do keystore, embutido no próprio diálogo.
        // keystorePathHolder[0] guarda o caminho escolhido (ou o já existente, na edição).
        final String[] keystorePathHolder = { base != null ? base.keystorePath() : null };
        Label keystoreFileLabel = new Label(base != null ? base.keystoreFileName() : "Nenhum arquivo selecionado");
        keystoreFileLabel.getStyleClass().add("card-subtitle");
        Button chooseFile = new Button("Escolher arquivo (.jks / .keystore)");
        chooseFile.getStyleClass().setAll("button", "secondary");
        chooseFile.setOnAction(e -> {
            FileChooser chooser = new FileChooser();
            chooser.setTitle("Selecionar keystore");
            chooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Keystore Android", "*.jks", "*.keystore"));
            File current = keystorePathHolder[0] != null ? new File(keystorePathHolder[0]).getParentFile() : null;
            chooser.setInitialDirectory(existingDirOrHome(current));
            File file = chooser.showOpenDialog(dialog.getDialogPane().getScene().getWindow());
            if (file != null) {
                keystorePathHolder[0] = file.getAbsolutePath();
                keystoreFileLabel.setText(file.getName());
            }
        });
        HBox fileRow = new HBox(10, chooseFile, keystoreFileLabel);
        fileRow.setMaxWidth(Double.MAX_VALUE);

        if (base != null) {
            name.setText(base.name());
            alias.setText(base.keyAlias());
            type.setValue(base.type());
            status.setValue(base.status());
            note.setText(base.note());
            linkedOdm.setValue(findOdmBySignatureId(base.id()));
            // Senhas ficam mascaradas no PasswordField (nunca em texto puro na tela).
            keystorePassword.setText(base.keystorePassword());
            keyPassword.setText(base.keyPassword());
        }

        GridPane grid = formGrid();
        int row = 0;
        grid.addRow(row++, new Label("Nome da assinatura"), name);
        grid.addRow(row++, new Label("Alias da chave"), alias);
        grid.addRow(row++, new Label("Arquivo"), fileRow);
        grid.addRow(row++, new Label("Senha do keystore"), keystorePassword);
        grid.addRow(row++, new Label("Senha da chave"), keyPassword);
        grid.addRow(row++, new Label("Tipo"), type);
        grid.addRow(row++, new Label("Status"), status);
        grid.addRow(row++, new Label("Vincular à ODM (opcional)"), linkedOdm);
        grid.addRow(row, new Label("Observação (opcional)"), note);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> {
            if (button != saveButton) {
                return null;
            }
            return new SigningForm(name.getText(), alias.getText(), keystorePathHolder[0],
                    keystorePassword.getText(), keyPassword.getText(),
                    type.getValue(), status.getValue(), note.getText(), linkedOdm.getValue());
        });

        return dialog.showAndWait();
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
    }

    private java.util.function.Predicate<Odm> odmPredicate() {
        String search = safeLower(odmSearchField.getText());
        return odm -> search.isBlank()
                || safeLower(odm.name()).contains(search)
                || safeLower(signatureNameFor(odm.signatureId())).contains(search);
    }

    @FXML
    private void onAddOdm() {
        showOdmDialog("Nova ODM", null).ifPresent(odmName -> {
            try {
                // A ODM nasce só com o nome; a assinatura é vinculada na seção de Assinaturas.
                createOdm.execute(new CreateOdmUseCase.Input(odmName));
                refreshOdms();
                flashStatus("ODM criada.");
            } catch (IllegalArgumentException e) {
                showError(e.getMessage());
            }
        });
    }

    @FXML
    private void onEditOdm() {
        Odm selected = odmTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Selecione uma ODM na tabela.");
            return;
        }
        showOdmDialog("Editar ODM", selected).ifPresent(odmName -> {
            try {
                updateOdm.execute(new UpdateOdmUseCase.Input(selected.id(), odmName));
                refreshOdms();
                flashStatus("ODM atualizada.");
            } catch (IllegalArgumentException e) {
                showError(e.getMessage());
            }
        });
    }

    @FXML
    private void onRemoveOdm() {
        Odm selected = odmTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Selecione uma ODM na tabela.");
            return;
        }
        if (!confirmDelete("Excluir a ODM \"" + selected.name()
                + "\"? Os terminais vinculados a ela ficarão sem ODM.")) {
            return;
        }
        removeOdm.execute(selected.id());
        refreshOdms();
        refreshTerminals();
        flashStatus("ODM excluída.");
    }

    private void refreshOdms() {
        odmMaster.setAll(listOdms.execute());
    }

    private Optional<String> showOdmDialog(String title, Odm base) {
        Dialog<String> dialog = newFormDialog(title);
        ButtonType saveButton = addSaveButton(dialog);

        TextField name = new TextField();
        name.setPromptText("Nome da ODM");
        if (base != null) {
            name.setText(base.name());
        }

        GridPane grid = formGrid();
        grid.addRow(0, new Label("Nome da ODM"), name);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> button == saveButton ? name.getText() : null);
        return dialog.showAndWait();
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

    // ===================== Client section =====================

    /** Nível de uma linha da árvore de clientes: o cliente, um app vinculado ou uma versão. */
    private enum ClientRowKind { CLIENT, APP, VERSION }

    /**
     * Linha da árvore de clientes. CLIENT é o pai; APP é um pacote vinculado àquele cliente;
     * VERSION é um Apk real. {@code current} segue o flag principal do pacote (selo "ATUAL").
     */
    private record ClientRow(ClientRowKind kind, String title, String subtitle,
                             ApkType type, ApkStatus status, boolean current,
                             Client client, Apk apk) {}

    private void configureClientTable() {
        clientTitleColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getValue().title()));
        clientSubtitleColumn.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getValue().subtitle()));
        clientCurrentColumn.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getValue().current()));
        clientTypeColumn.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getValue().type()));
        clientStatusColumn.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getValue().status()));

        clientCurrentColumn.setCellFactory(col -> currentBadgeCell());
        clientTypeColumn.setCellFactory(col -> treeBadgeCell(type ->
                type == ApkType.GERTEC ? "badge-gertec" : "badge-cliente"));
        clientStatusColumn.setCellFactory(col -> treeBadgeCell(status -> switch (status) {
            case ATIVO -> "badge-ativo";
            case PENDENTE -> "badge-pendente";
            case INATIVO -> "badge-inativo";
        }));
        clientActionColumn.setCellFactory(col -> treeActionCell(
                row -> row.kind() == ClientRowKind.VERSION,
                row -> showApkDetails(row.apk())));

        clientTree.setShowRoot(false);
        clientTree.setRoot(new TreeItem<>(new ClientRow(ClientRowKind.CLIENT, "", "", null, null, false, null, null)));

        clientSearchField.textProperty().addListener((o, old, val) -> rebuildClientTree());
    }

    /**
     * (Re)constrói a árvore de clientes: cada cliente é um pai; abaixo dele os aplicativos
     * (pacotes) vinculados (APKs do tipo CLIENTE com aquele nome) e, dentro de cada app, as
     * versões — da mais nova para a mais antiga, com o selo "ATUAL" na versão principal.
     */
    private void rebuildClientTree() {
        if (clientTree == null || clientTree.getRoot() == null) {
            return; // árvore ainda não configurada (chamado durante o boot)
        }
        String search = safeLower(clientSearchField.getText());

        List<TreeItem<ClientRow>> clientItems = new ArrayList<>();
        for (Client client : clientMaster) {
            // APKs vinculados a este cliente, agrupados por pacote.
            Map<String, List<Apk>> byPackage = new LinkedHashMap<>();
            for (Apk apk : apkMaster) {
                if (apk.type() == ApkType.CLIENTE && client.name().equalsIgnoreCase(apk.client())) {
                    byPackage.computeIfAbsent(apk.packageName(), k -> new ArrayList<>()).add(apk);
                }
            }

            boolean nameMatches = search.isBlank() || safeLower(client.name()).contains(search);

            List<TreeItem<ClientRow>> appItems = new ArrayList<>();
            for (Map.Entry<String, List<Apk>> entry : byPackage.entrySet()) {
                List<Apk> versions = entry.getValue();
                boolean appMatches = nameMatches
                        || safeLower(entry.getKey()).contains(search)
                        || versions.stream().anyMatch(a -> safeLower(a.label()).contains(search)
                        || safeLower(a.versionName()).contains(search)
                        || safeLower(a.apkFileName()).contains(search));
                if (!appMatches) {
                    continue;
                }

                versions.sort(Comparator.comparingLong(Apk::versionCode).reversed());
                Apk latest = versions.get(0);
                String countText = versions.size() == 1 ? "1 versão" : versions.size() + " versões";
                TreeItem<ClientRow> appItem = new TreeItem<>(new ClientRow(ClientRowKind.APP,
                        latest.label(), entry.getKey() + "  ·  " + countText, null, null, false, client, latest));
                appItem.setExpanded(true);
                for (Apk version : versions) {
                    appItem.getChildren().add(new TreeItem<>(new ClientRow(ClientRowKind.VERSION,
                            "v" + version.versionName(), version.apkFileName(),
                            version.type(), version.status(), version.principal(), client, version)));
                }
                appItems.add(appItem);
            }

            // Não mostra o cliente se a busca não casou nem com ele nem com nenhum app.
            if (!nameMatches && appItems.isEmpty()) {
                continue;
            }

            String appCount = appItems.isEmpty() ? "Nenhum aplicativo vinculado"
                    : (appItems.size() == 1 ? "1 aplicativo" : appItems.size() + " aplicativos");
            TreeItem<ClientRow> clientItem = new TreeItem<>(new ClientRow(ClientRowKind.CLIENT,
                    client.name(), appCount, null, null, false, client, null));
            clientItem.setExpanded(true);
            clientItem.getChildren().setAll(appItems);
            clientItems.add(clientItem);
        }

        clientItems.sort(Comparator.comparing(i -> safeLower(i.getValue().title())));
        clientTree.getRoot().getChildren().setAll(clientItems);
    }

    /** Cliente selecionado na árvore (apenas em linha-pai de cliente), ou null. */
    private Client selectedClient() {
        TreeItem<ClientRow> item = clientTree.getSelectionModel().getSelectedItem();
        return item != null && item.getValue().kind() == ClientRowKind.CLIENT ? item.getValue().client() : null;
    }

    /** Versão selecionada na árvore de clientes (apenas em linha-folha de versão), ou null. */
    private Apk selectedClientApk() {
        TreeItem<ClientRow> item = clientTree.getSelectionModel().getSelectedItem();
        return item != null && item.getValue().kind() == ClientRowKind.VERSION ? item.getValue().apk() : null;
    }

    @FXML
    private void onAddClient() {
        showClientDialog("Novo cliente", null).ifPresent(name -> {
            try {
                createClient.execute(new CreateClientUseCase.Input(name));
                refreshClients();
                flashStatus("Cliente cadastrado.");
            } catch (IllegalArgumentException e) {
                showError(e.getMessage());
            }
        });
    }

    @FXML
    private void onEditClient() {
        Client selected = selectedClient();
        if (selected == null) {
            showError("Selecione um cliente (linha-pai) na árvore.");
            return;
        }
        showClientDialog("Editar cliente", selected).ifPresent(name -> {
            try {
                updateClient.execute(new UpdateClientUseCase.Input(selected.id(), name));
                refreshClients();
                flashStatus("Cliente atualizado.");
            } catch (IllegalArgumentException e) {
                showError(e.getMessage());
            }
        });
    }

    @FXML
    private void onRemoveClient() {
        Client selected = selectedClient();
        if (selected == null) {
            showError("Selecione um cliente (linha-pai) na árvore.");
            return;
        }
        if (!confirmDelete("Excluir o cliente \"" + selected.name()
                + "\"? Os APKs já vinculados continuarão com o nome registrado.")) {
            return;
        }
        removeClient.execute(selected.id());
        refreshClients();
        flashStatus("Cliente excluído.");
    }

    @FXML
    private void onSetPrincipalClientApk() {
        Apk selected = selectedClientApk();
        if (selected == null) {
            showError("Selecione uma versão (linha-folha) para tornar principal.");
            return;
        }
        setPrincipalApk.execute(selected.id());
        refreshApks(); // recarrega APKs e reconstrói ambas as árvores (APK e clientes)
        flashStatus("Versão v" + selected.versionName() + " definida como principal.");
    }

    private void refreshClients() {
        clientMaster.setAll(listClients.execute());
        rebuildClientTree();
    }

    private Optional<String> showClientDialog(String title, Client base) {
        Dialog<String> dialog = newFormDialog(title);
        ButtonType saveButton = addSaveButton(dialog);

        TextField name = new TextField();
        name.setPromptText("Nome do cliente");
        if (base != null) {
            name.setText(base.name());
        }

        GridPane grid = formGrid();
        grid.addRow(0, new Label("Nome do cliente"), name);
        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(button -> button == saveButton ? name.getText() : null);
        return dialog.showAndWait();
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

    // --- Tree (TreeTableView de APKs) cell helpers ---

    /** Badge de enum para qualquer árvore; vazio em linhas sem o valor (item null). */
    private <R, E extends Enum<E>> TreeTableCell<R, E> treeBadgeCell(java.util.function.Function<E, String> styleResolver) {
        return new TreeTableCell<>() {
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

    /** Selo "ATUAL" exibido apenas na versão vigente (current == true). */
    private <R> TreeTableCell<R, Boolean> currentBadgeCell() {
        return new TreeTableCell<>() {
            @Override
            protected void updateItem(Boolean current, boolean empty) {
                super.updateItem(current, empty);
                if (empty || !Boolean.TRUE.equals(current)) {
                    setGraphic(null);
                    setText(null);
                    return;
                }
                Label badge = new Label("ATUAL");
                badge.getStyleClass().setAll("badge", "badge-ativo");
                setGraphic(badge);
                setText(null);
            }
        };
    }

    /** Link "VER" para qualquer árvore; só aparece nas linhas em que {@code showFor} for verdadeiro. */
    private <R> TreeTableCell<R, Void> treeActionCell(java.util.function.Predicate<R> showFor,
                                                      java.util.function.Consumer<R> action) {
        return new TreeTableCell<>() {
            private final Button button = new Button("VER");
            {
                button.getStyleClass().setAll("action-link");
                button.setOnAction(e -> {
                    R row = getTableRow() == null ? null : getTableRow().getItem();
                    if (row != null) {
                        action.accept(row);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                R row = getTableRow() == null ? null : getTableRow().getItem();
                setGraphic(empty || row == null || !showFor.test(row) ? null : button);
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

    // ===================== Dialog helpers =====================

    /** Cria um diálogo de formulário padrão (sem header), reutilizado por todas as seções. */
    private <T> Dialog<T> newFormDialog(String title) {
        Dialog<T> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        return dialog;
    }

    /** Adiciona o botão "Salvar" (OK) e "Cancelar" e devolve o tipo do Salvar. */
    private ButtonType addSaveButton(Dialog<?> dialog) {
        ButtonType saveButton = new ButtonType("Salvar", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(saveButton, ButtonType.CANCEL);
        return saveButton;
    }

    private GridPane formGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        grid.setPadding(new Insets(16));
        return grid;
    }

    /** Pede confirmação antes de uma exclusão; devolve true se o usuário confirmar. */
    private boolean confirmDelete(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Confirmar exclusão");
        alert.setHeaderText(null);
        return alert.showAndWait().filter(b -> b == ButtonType.OK).isPresent();
    }

    /** Pede confirmação genérica (não destrutiva); devolve true se o usuário confirmar. */
    private boolean confirmAction(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.OK, ButtonType.CANCEL);
        alert.setTitle("Confirmar");
        alert.setHeaderText(null);
        return alert.showAndWait().filter(b -> b == ButtonType.OK).isPresent();
    }

    /** Mostra uma mensagem de sucesso transitória que some sozinha após alguns segundos. */
    private void flashStatus(String message) {
        statusLabel.setText(message);
        statusLabel.setOpacity(1);
        statusLabel.setVisible(true);
        statusLabel.setManaged(true);

        PauseTransition pause = new PauseTransition(Duration.seconds(2.5));
        FadeTransition fade = new FadeTransition(Duration.millis(400), statusLabel);
        fade.setFromValue(1);
        fade.setToValue(0);
        fade.setOnFinished(e -> {
            statusLabel.setVisible(false);
            statusLabel.setManaged(false);
        });
        pause.setOnFinished(e -> fade.play());
        pause.play();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Dados inválidos");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
