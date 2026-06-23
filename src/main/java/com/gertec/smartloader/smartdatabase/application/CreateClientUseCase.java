package com.gertec.smartloader.smartdatabase.application;

import com.gertec.smartloader.smartdatabase.domain.entity.Client;
import com.gertec.smartloader.smartdatabase.domain.repository.ClientRepository;

public class CreateClientUseCase {

    private final ClientRepository repository;

    public CreateClientUseCase(ClientRepository repository) {
        this.repository = repository;
    }

    public record Input(String name) {}

    public Client execute(Input input) {
        if (input.name() == null || input.name().isBlank())
            throw new IllegalArgumentException("Cliente deve ter algum nome");

        if (repository.existsByName(input.name().trim()))
            throw new IllegalArgumentException("já existe um cliente com este nome");

        Client client = Client.create(input.name().trim());
        repository.save(client);
        return client;
    }
}
