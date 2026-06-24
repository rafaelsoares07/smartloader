package com.gertec.smartloader.smartdatabase.domain.entity;

import java.util.UUID;


public final class Client {

    private final String id;
    private final String name;


    public Client(String id, String name) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("id é obrigatório");
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Cliente deve ter algum nome");

        this.id = id;
        this.name = name;
    }


    public static Client create(String name) {
        return new Client(UUID.randomUUID().toString(), name);
    }

    public String id() { return id; }
    public String name() { return name; }
}
