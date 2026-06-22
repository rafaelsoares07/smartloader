package com.gertec.smartloader.smartdatabase.domain.entity;

import java.util.UUID;

// Entidade pura: apenas Java, sem JavaFX nem Spring. As invariantes vivem aqui.
//
// Conceito de ODM: agrupa terminais e carrega (1:1) a assinatura que será aplicada.
// O vínculo com a Signature é por id (String UUID), nunca entidade embutida.
// A assinatura é OPCIONAL: uma ODM pode nascer sem assinatura e recebê-la depois.
public final class Odm {

    private final String id;
    private final String name;
    private final String signatureId; // opcional — id da SigningProfile vinculada (1:1)

    // Construtor de reconstrução: recria uma ODM já existente (veio da persistência).
    public Odm(String id, String name, String signatureId) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("id é obrigatório");
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("ODM deve ter algum nome");

        this.id = id;
        this.name = name;
        this.signatureId = nullToEmpty(signatureId);
    }

    // Fábrica para uma ODM NOVA: o domínio gera o id e nasce sem assinatura vinculada.
    public static Odm create(String name) {
        return new Odm(UUID.randomUUID().toString(), name, "");
    }

    // Devolve uma cópia com a assinatura vinculada, preservando o restante.
    public Odm withSignature(String signatureId) {
        return new Odm(id, name, signatureId);
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    public String id() { return id; }
    public String name() { return name; }
    public String signatureId() { return signatureId; }

    public boolean hasSignature() {
        return signatureId != null && !signatureId.isBlank();
    }
}
