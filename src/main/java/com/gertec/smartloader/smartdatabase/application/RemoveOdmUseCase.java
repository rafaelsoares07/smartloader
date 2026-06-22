package com.gertec.smartloader.smartdatabase.application;

import com.gertec.smartloader.smartdatabase.domain.repository.OdmRepository;

public class RemoveOdmUseCase {

    private final OdmRepository repository;

    public RemoveOdmUseCase(OdmRepository repository) {
        this.repository = repository;
    }

    public void execute(String id) {
        repository.deleteById(id);
    }
}
