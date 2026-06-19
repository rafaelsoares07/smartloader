package com.gertec.smartloader.smartdatabase.application;

import com.gertec.smartloader.smartdatabase.domain.entity.Apk;
import com.gertec.smartloader.smartdatabase.domain.repository.ApkRepository;
import java.util.List;

public class ListApksUseCase {
    private final ApkRepository repository;
    public ListApksUseCase(ApkRepository repository) { this.repository = repository; }
    public List<Apk> execute() { return repository.findAll(); }
}