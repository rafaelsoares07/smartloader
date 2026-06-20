package com.gertec.smartloader.smartdatabase.domain.entity;

import com.gertec.smartloader.smartdatabase.domain.enums.TerminalType;
import java.util.UUID;

public final class TerminalModel {

    private final String id;
    private final String terminalName;
    private final TerminalType terminalType;

    // Construtor de reconstrução: recria um terminal que já existe (veio da persistência).
    public TerminalModel(String id, String terminalName, TerminalType terminalType) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("id é obrigatório");
        if (terminalName == null || terminalName.isBlank())
            throw new IllegalArgumentException("Terminal deve ter algum nome");
        if (terminalType == null)
            throw new IllegalArgumentException("Terminal deve ter um tipo pré-definido");

        this.id = id;
        this.terminalName = terminalName;
        this.terminalType = terminalType;
    }

    public static TerminalModel create(String terminalName, TerminalType terminalType) {
        return new TerminalModel(UUID.randomUUID().toString(), terminalName, terminalType);
    }

    public String id() { return id; }
    public String terminalName() { return terminalName; }
    public TerminalType terminalType() { return terminalType; }
}