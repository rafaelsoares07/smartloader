package com.gertec.smartloader.smartdatabase.application;

import com.gertec.smartloader.smartdatabase.domain.entity.Odm;
import com.gertec.smartloader.smartdatabase.domain.repository.OdmRepository;

public class CreateOdmUseCase {

    private final OdmRepository repository;

    public CreateOdmUseCase(OdmRepository repository) {
        this.repository = repository;
    }

    public record Input(String name) {}

    public Odm execute(Input input) {
        if (input.name() == null || input.name().isBlank())
            throw new IllegalArgumentException("ODM deve ter algum nome");

        if (repository.existsByName(input.name().trim()))
            throw new IllegalArgumentException("já existe uma ODM com este nome");

        // A ODM nasce sem assinatura o vínculo é feito depois via AssignSignatureToOdmUseCase.
        Odm odm = Odm.create(input.name().trim());
        repository.save(odm);
        return odm;
    }
}
