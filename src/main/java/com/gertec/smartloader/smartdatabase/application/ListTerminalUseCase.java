package com.gertec.smartloader.smartdatabase.application;

import com.gertec.smartloader.smartdatabase.domain.entity.TerminalModel;
import com.gertec.smartloader.smartdatabase.domain.repository.TerminalModelRepository;
import java.util.List;

public class ListTerminalUseCase {

    private final TerminalModelRepository repository;

    public ListTerminalUseCase(TerminalModelRepository repository) {
        this.repository = repository;
    }

    public List<TerminalModel> execute() {
        return repository.findAll();
    }
}