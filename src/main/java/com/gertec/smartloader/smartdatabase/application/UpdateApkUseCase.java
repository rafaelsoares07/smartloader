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
        if (repository.findById(input.id()).isEmpty())
            throw new IllegalArgumentException("APK não encontrado para edição");

        // Reconstrói a entidade mantendo o mesmo id; a validação vive no domínio.
        Apk updated = new Apk(input.id(), input.apkFileName(), input.packageName(),
                input.label(), input.versionName(), input.versionCode(), input.client(),
                input.type(), input.status(), input.cloudPath());
        repository.save(updated);
        return updated;
    }
}
