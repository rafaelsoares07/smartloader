package com.gertec.smartloader.smartdatabase.application;

import com.gertec.smartloader.smartdatabase.domain.entity.Apk;
import com.gertec.smartloader.smartdatabase.domain.enums.ApkStatus;
import com.gertec.smartloader.smartdatabase.domain.enums.ApkType;
import com.gertec.smartloader.smartdatabase.domain.repository.ApkRepository;

public class UpdateApkUseCase {

    private final ApkRepository repository;

    public UpdateApkUseCase(ApkRepository repository) {
        this.repository = repository;
    }

    public record Input(String id, String apkFileName, String packageName, String label,
                        String versionName, long versionCode, String client,
                        ApkType type, ApkStatus status, String cloudPath) {}

    public Apk execute(Input input) {
        Apk existing = repository.findById(input.id())
                .orElseThrow(() -> new IllegalArgumentException("APK não encontrado para edição"));

        // Reconstrói a entidade mantendo o mesmo id e PRESERVANDO o flag principal
        // (a troca de versão principal é feita por SetPrincipalApkUseCase, não pela edição).
        Apk updated = new Apk(input.id(), input.apkFileName(), input.packageName(),
                input.label(), input.versionName(), input.versionCode(), input.client(),
                input.type(), input.status(), input.cloudPath(), existing.principal());
        repository.save(updated);
        return updated;
    }
}
