package com.gertec.smartloader.smartdatabase.application;

import com.gertec.smartloader.smartdatabase.domain.entity.TerminalModel;
import com.gertec.smartloader.smartdatabase.domain.enums.TerminalType;
import com.gertec.smartloader.smartdatabase.domain.repository.TerminalModelRepository;

public class UpdateTerminalUseCase {

    private final TerminalModelRepository repository;

    public UpdateTerminalUseCase(TerminalModelRepository repository) {
        this.repository = repository;
    }

    public record Input(String id, String terminalName, TerminalType terminalType, String odmId) {}

    public TerminalModel execute(Input input) {
        if (repository.findById(input.id()).isEmpty())
            throw new IllegalArgumentException("Terminal não encontrado para edição");

        TerminalModel updated = new TerminalModel(input.id(), input.terminalName(), input.terminalType(), input.odmId());
        repository.save(updated);
        return updated;
    }
}
