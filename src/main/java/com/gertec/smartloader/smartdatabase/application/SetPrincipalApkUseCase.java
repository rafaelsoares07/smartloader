package com.gertec.smartloader.smartdatabase.application;

import com.gertec.smartloader.smartdatabase.domain.entity.Apk;
import com.gertec.smartloader.smartdatabase.domain.repository.ApkRepository;

public class SetPrincipalApkUseCase {

    private final ApkRepository repository;

    public SetPrincipalApkUseCase(ApkRepository repository) {
        this.repository = repository;
    }

    public void execute(String apkId) {
        Apk target = repository.findById(apkId)
                .orElseThrow(() -> new IllegalArgumentException("APK não encontrado"));

        for (Apk apk : repository.findAll()) {
            if (apk.packageName().equalsIgnoreCase(target.packageName())) {
                boolean shouldBePrincipal = apk.id().equals(apkId);
                if (apk.principal() != shouldBePrincipal) {
                    repository.save(apk.withPrincipal(shouldBePrincipal));
                }
            }
        }
    }
}
