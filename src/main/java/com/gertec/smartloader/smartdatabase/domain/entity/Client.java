package com.gertec.smartloader.smartdatabase.domain.entity;

import java.util.UUID;

// Entidade pura: apenas Java, sem JavaFX nem Spring. As invariantes vivem aqui.
//
// Conceito de Cliente: dono/solicitante de um APK. Um APK ou é "padrão Gertec" (sem cliente)
// ou pertence a um cliente cadastrado. O vínculo é feito na UI ao cadastrar o APK.
public final class Client {

    private final String id;
    private final String name;

    // Construtor de reconstrução: recria um cliente já existente (veio da persistência).
    public Client(String id, String name) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("id é obrigatório");
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("Cliente deve ter algum nome");

        this.id = id;
        this.name = name;
    }

    // Fábrica para um cliente NOVO: o domínio gera o próprio id.
    public static Client create(String name) {
        return new Client(UUID.randomUUID().toString(), name);
    }

    public String id() { return id; }
    public String name() { return name; }
}
