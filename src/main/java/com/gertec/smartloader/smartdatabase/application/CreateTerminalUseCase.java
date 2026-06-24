package com.gertec.smartloader.smartdatabase.application;

import com.gertec.smartloader.smartdatabase.domain.entity.TerminalModel;
import com.gertec.smartloader.smartdatabase.domain.enums.TerminalType;
import com.gertec.smartloader.smartdatabase.domain.repository.OdmRepository;
import com.gertec.smartloader.smartdatabase.domain.repository.TerminalModelRepository;

public class CreateTerminalUseCase {

    private final TerminalModelRepository repository;
    private final OdmRepository odmRepository;

    public CreateTerminalUseCase(TerminalModelRepository repository, OdmRepository odmRepository) {
        this.repository = repository;
        this.odmRepository = odmRepository;
    }

    public record Input(String terminalName, TerminalType terminalType, String odmId) {}

    public TerminalModel execute(Input input) {

        if (odmRepository.findById(input.odmId()).isEmpty())
            throw new IllegalArgumentException("ODM não encontrada para o terminal");

        TerminalModel terminal = TerminalModel.create(input.terminalName(), input.terminalType(), input.odmId());
        repository.save(terminal);
        // O pulo do gato continua valendo: pedimos pra salvar sem saber COMO será salvo.
        // Memória, Drive, Firebase ou Postgres — o use case não muda.
        return terminal;
    }
}
