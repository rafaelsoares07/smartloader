package com.gertec.smartloader.smartdatabase.application;

import com.gertec.smartloader.smartdatabase.domain.entity.TerminalModel;
import com.gertec.smartloader.smartdatabase.domain.enums.TerminalType;
import com.gertec.smartloader.smartdatabase.domain.repository.TerminalModelRepository;

public class CreateTerminalUseCase {

    private final TerminalModelRepository repository;

    public CreateTerminalUseCase(TerminalModelRepository repository) {
        this.repository = repository;
    }

    public record Input(String terminalName, TerminalType terminalType) {}

    public TerminalModel execute(Input input) {
        TerminalModel terminal = TerminalModel.create(input.terminalName(), input.terminalType());
        repository.save(terminal);
        // O pulo do gato continua valendo: pedimos pra salvar sem saber COMO será salvo.
        // Memória, Drive, Firebase ou Postgres — o use case não muda.
        return terminal;
    }
}