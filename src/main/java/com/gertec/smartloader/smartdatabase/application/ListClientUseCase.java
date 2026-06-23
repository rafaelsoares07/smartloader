package com.gertec.smartloader.smartdatabase.application;

import com.gertec.smartloader.smartdatabase.domain.entity.Client;
import com.gertec.smartloader.smartdatabase.domain.repository.ClientRepository;

import java.util.List;

public class ListClientUseCase {

    private final ClientRepository repository;

    public ListClientUseCase(ClientRepository repository) {
        this.repository = repository;
    }

    public List<Client> execute() {
        return repository.findAll();
    }
}
