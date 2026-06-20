package com.gertec.smartloader.smartdatabase.application;

import com.gertec.smartloader.smartdatabase.domain.repository.TerminalModelRepository;

public class RemoveTerminalUseCase {

    private final TerminalModelRepository repository;

    public RemoveTerminalUseCase(TerminalModelRepository repository) {
        this.repository = repository;
    }

    public void execute(String id) {
        repository.deleteById(id);
    }
}