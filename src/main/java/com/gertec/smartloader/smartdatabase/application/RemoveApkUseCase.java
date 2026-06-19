package com.gertec.smartloader.smartdatabase.application;

import com.gertec.smartloader.smartdatabase.domain.repository.ApkRepository;

public class RemoveApkUseCase {
    private final ApkRepository repository;
    public RemoveApkUseCase(ApkRepository repository) { this.repository = repository; }
    public void execute(String id) { repository.deleteById(id); }
}