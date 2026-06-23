package com.gertec.smartloader.smartdatabase.application;

import com.gertec.smartloader.smartdatabase.domain.entity.Apk;
import com.gertec.smartloader.smartdatabase.domain.enums.ApkStatus;
import com.gertec.smartloader.smartdatabase.domain.enums.ApkType;
import com.gertec.smartloader.smartdatabase.domain.repository.ApkRepository;

public class CreateApkUseCase {

    private final ApkRepository repository;

    public CreateApkUseCase(ApkRepository apkRepository) {
        this.repository = apkRepository;
    }

    public record Input(String apkFileName, String packageName, String label,
                        String versionName, long versionCode, String client,
                        ApkType type, ApkStatus status, String cloudPath) {}

    public Apk execute(Input input) {
        Apk apk = Apk.create(input.apkFileName(), input.packageName(), input.label(),
                input.versionName(), input.versionCode(), input.client(),
                input.type(), input.status(), input.cloudPath());

        // Garante que todo pacote tenha uma versão principal: a PRIMEIRA versão de um pacote
        // nasce principal. As demais nascem normais e são promovidas via SetPrincipalApkUseCase.
        boolean packageHasVersion = repository.findAll().stream()
                .anyMatch(a -> a.packageName().equalsIgnoreCase(input.packageName()));
        if (!packageHasVersion) {
            apk = apk.withPrincipal(true);
        }

        repository.save(apk);
        // Aqui é o pulo do gato: o repository tem que ser implementado, então só estamos
        // pedindo para salvar e não falando COMO isso vai ser salvo. Nesse ponto não nos importa.
        return apk;
    }
}
