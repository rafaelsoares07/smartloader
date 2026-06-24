package com.gertec.smartloader.smartdatabase.application;

import com.gertec.smartloader.smartdatabase.domain.entity.Client;
import com.gertec.smartloader.smartdatabase.domain.repository.ClientRepository;

public class UpdateClientUseCase {

    private final ClientRepository repository;

    public UpdateClientUseCase(ClientRepository repository) {
        this.repository = repository;
    }

    public record Input(String id, String name) {}

    public Client execute(Input input) {
        Client existing = repository.findById(input.id())
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado para edição"));

        if (input.name() == null || input.name().isBlank())
            throw new IllegalArgumentException("Cliente deve ter algum nome");

        String newName = input.name().trim();
        if (!existing.name().equalsIgnoreCase(newName) && repository.existsByName(newName))
            throw new IllegalArgumentException("já existe um cliente com este nome");

        Client updated = new Client(existing.id(), newName);
        repository.update(updated);
        return updated;
    }
}
