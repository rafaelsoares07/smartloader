package com.gertec.smartloader.smartdatabase.application;

import com.gertec.smartloader.smartdatabase.domain.entity.Odm;
import com.gertec.smartloader.smartdatabase.domain.repository.OdmRepository;

public class UpdateOdmUseCase {

    private final OdmRepository repository;

    public UpdateOdmUseCase(OdmRepository repository) {
        this.repository = repository;
    }

    public record Input(String id, String name) {}

    public Odm execute(Input input) {
        Odm existing = repository.findById(input.id())
                .orElseThrow(() -> new IllegalArgumentException("ODM não encontrada para edição"));

        if (input.name() == null || input.name().isBlank())
            throw new IllegalArgumentException("ODM deve ter algum nome");

        String newName = input.name().trim();
        if (!existing.name().equalsIgnoreCase(newName) && repository.existsByName(newName))
            throw new IllegalArgumentException("já existe uma ODM com este nome");

        Odm updated = new Odm(existing.id(), newName, existing.signatureId());
        repository.update(updated);
        return updated;
    }
}
