package com.gertec.smartloader.smartdatabase.application;

import com.gertec.smartloader.smartdatabase.domain.entity.Apk;
import com.gertec.smartloader.smartdatabase.domain.repository.ApkRepository;

import java.util.Comparator;
import java.util.Optional;

public class RemoveApkUseCase {
    private final ApkRepository repository;

    public RemoveApkUseCase(ApkRepository repository) {
        this.repository = repository;
    }

    public void execute(String id) {
        Optional<Apk> removed = repository.findById(id);
        repository.deleteById(id);

        // Mantém a invariante "todo pacote tem uma versão principal": se a versão excluída
        // era a principal e ainda restam irmãs, promove a mais recente (maior versionCode).
        if (removed.isEmpty() || !removed.get().principal()) {
            return;
        }
        String packageName = removed.get().packageName();
        repository.findAll().stream()
                .filter(a -> a.packageName().equalsIgnoreCase(packageName))
                .max(Comparator.comparingLong(Apk::versionCode))
                .ifPresent(next -> repository.save(next.withPrincipal(true)));
    }
}
