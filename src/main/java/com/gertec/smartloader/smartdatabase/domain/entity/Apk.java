package com.gertec.smartloader.smartdatabase.domain.entity;

import java.util.UUID;

// Entidade pura apenas java, não conhece frameworks,deve ter validações internas, nõ botamos o jpa aqui porque ia travar e depender dele.


public final class Apk {

    private final String id;
    private final String packageName;
    private String label;
    private String versionName;
    private final long versionCode;

    // Construtor "de reconstrução": usado para recriar um Apk vindo da persistência.
    public Apk(String id, String packageName, String label, String versionName, long versionCode) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("id é obrigatório");
        if (packageName == null || packageName.isBlank())
            throw new IllegalArgumentException("packageName é obrigatório");
        if (label == null || label.isBlank())
            throw new IllegalArgumentException("label é obrigatório");
        if (versionCode < 0)
            throw new IllegalArgumentException("versionCode não pode ser negativo");

        this.id = id;
        this.packageName = packageName;
        this.label = label;
        this.versionName = versionName;
        this.versionCode = versionCode;
    }

    // Fábrica para um Apk NOVO: o domínio gera o próprio id (não depende do banco).
    public static Apk create(String packageName, String label, String versionName, long versionCode) {
        return new Apk(UUID.randomUUID().toString(), packageName, label, versionName, versionCode);
    }

    // Comportamento, não setter solto: a entidade controla COMO ela muda.
    public void rename(String newLabel) {
        if (newLabel == null || newLabel.isBlank())
            throw new IllegalArgumentException("label inválido");
        this.label = newLabel;
    }

    public String id() { return id; }
    public String packageName() { return packageName; }
    public String label() { return label; }
    public String versionName() { return versionName; }
    public long versionCode() { return versionCode; }
}