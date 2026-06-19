package com.gertec.smartloader.smartdatabase.application;

import com.gertec.smartloader.smartdatabase.domain.entity.Apk;
import com.gertec.smartloader.smartdatabase.domain.repository.ApkRepository;

public class CreateApkUseCase {

    private final ApkRepository repository;

    public CreateApkUseCase(ApkRepository apkRepository){
        this.repository = apkRepository;
    }

    public record Input(String packageName, String label, String versionName, long versionCode) {}

    public Apk execute(Input input) {
        Apk apk = Apk.create(input.packageName(), input.label(),
                input.versionName(), input.versionCode());
        repository.save(apk);
        // Aqui é o pulo do gato, o repository tem que ser implementado então só estamos pedindo para salvar
        // e não falando como isso vai ser salvo, nesse ponto não nos importa
        return apk;
    }
}
