package com.gertec.smartloader.smartdatabase.domain.entity;

import com.gertec.smartloader.smartdatabase.domain.enums.TerminalType;
import java.util.UUID;

public final class TerminalModel {

    private final String id;
    private final String terminalName;
    private final TerminalType terminalType;
    private final String odmId;


    public TerminalModel(String id, String terminalName, TerminalType terminalType, String odmId) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("id é obrigatório");
        if (terminalName == null || terminalName.isBlank())
            throw new IllegalArgumentException("Terminal deve ter algum nome");
        if (terminalType == null)
            throw new IllegalArgumentException("Terminal deve ter um tipo pré-definido");
        if (odmId == null || odmId.isBlank())
            throw new IllegalArgumentException("Terminal deve pertencer a uma ODM");

        this.id = id;
        this.terminalName = terminalName;
        this.terminalType = terminalType;
        this.odmId = odmId;
    }

    public static TerminalModel create(String terminalName, TerminalType terminalType, String odmId) {
        return new TerminalModel(UUID.randomUUID().toString(), terminalName, terminalType, odmId);
    }

    public String id() { return id; }
    public String terminalName() { return terminalName; }
    public TerminalType terminalType() { return terminalType; }
    public String odmId() { return odmId; }
}