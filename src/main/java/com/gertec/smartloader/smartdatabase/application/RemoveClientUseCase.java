package com.gertec.smartloader.smartdatabase.application;

import com.gertec.smartloader.smartdatabase.domain.repository.ClientRepository;

public class RemoveClientUseCase {

    private final ClientRepository repository;

    public RemoveClientUseCase(ClientRepository repository) {
        this.repository = repository;
    }

    public void execute(String id) {
        repository.deleteById(id);
    }
}
