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
                        String versionName, long versionCode, String clientId,
                        ApkType type, ApkStatus status, String cloudPath) {}

    public Apk execute(Input input) {
        // Um app é identificado pelo packageName. Todas as versões de um mesmo app compartilham
        // o tipo (GERTEC/CLIENTE) e o cliente definidos na PRIMEIRA versão. Uma nova versão não
        // pode mudar de tipo nem ser atribuída a outro cliente.
        var existingVersions = repository.findAll().stream()
                .filter(a -> a.packageName().equalsIgnoreCase(input.packageName()))
                .toList();
        if (!existingVersions.isEmpty()) {
            Apk reference = existingVersions.get(0);
            ApkTypeConsistency.assertSameTypeAndClient(reference, input.type(), input.clientId());
        }

        Apk apk = Apk.create(input.apkFileName(), input.packageName(), input.label(),
                input.versionName(), input.versionCode(), input.clientId(),
                input.type(), input.status(), input.cloudPath());

        // Garante que todo pacote tenha uma versão principal: a PRIMEIRA versão de um pacote
        // nasce principal. As demais nascem normais e são promovidas via SetPrincipalApkUseCase.
        if (existingVersions.isEmpty()) {
            apk = apk.withPrincipal(true);
        }

        repository.save(apk);
        // Aqui é o pulo do gato: o repository tem que ser implementado, então só estamos
        // pedindo para salvar e não falando COMO isso vai ser salvo. Nesse ponto não nos importa.
        return apk;
    }
}
