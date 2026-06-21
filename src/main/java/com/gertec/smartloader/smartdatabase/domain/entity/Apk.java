package com.gertec.smartloader.smartdatabase.domain.entity;

import com.gertec.smartloader.smartdatabase.domain.enums.ApkStatus;
import com.gertec.smartloader.smartdatabase.domain.enums.ApkType;

import java.util.UUID;

// Entidade pura apenas java, não conhece frameworks, deve ter validações internas.
// Não botamos JPA aqui porque ia travar e depender dele.
public final class Apk {

    private final String id;
    private final String apkFileName;   // nome do arquivo .apk (ex.: meuapp-1.0.0.apk)
    private final String packageName;   // ex.: com.gertec.app
    private final String label;         // nome amigável exibido
    private final String versionName;   // ex.: 1.0.0
    private final long versionCode;     // ex.: 100
    private final String client;        // cliente dono/solicitante do APK
    private final ApkType type;         // GERTEC ou CLIENTE
    private final ApkStatus status;     // ATIVO, PENDENTE, INATIVO
    private final String cloudPath;     // referência/caminho do arquivo em nuvem

    // Construtor "de reconstrução": usado para recriar um Apk vindo da persistência
    // ou para regravar um Apk editado mantendo o mesmo id.
    public Apk(String id, String apkFileName, String packageName, String label,
               String versionName, long versionCode, String client,
               ApkType type, ApkStatus status, String cloudPath) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("id é obrigatório");
        if (apkFileName == null || apkFileName.isBlank())
            throw new IllegalArgumentException("nome do APK é obrigatório");
        if (packageName == null || packageName.isBlank())
            throw new IllegalArgumentException("packageName é obrigatório");
        if (label == null || label.isBlank())
            throw new IllegalArgumentException("label é obrigatório");
        if (versionCode < 0)
            throw new IllegalArgumentException("versionCode não pode ser negativo");
        if (type == null)
            throw new IllegalArgumentException("tipo do APK é obrigatório");
        if (status == null)
            throw new IllegalArgumentException("status do APK é obrigatório");

        this.id = id;
        this.apkFileName = apkFileName;
        this.packageName = packageName;
        this.label = label;
        this.versionName = nullToEmpty(versionName);
        this.versionCode = versionCode;
        this.client = nullToEmpty(client);
        this.type = type;
        this.status = status;
        this.cloudPath = nullToEmpty(cloudPath);
    }

    // Fábrica para um Apk NOVO: o domínio gera o próprio id (não depende do banco).
    public static Apk create(String apkFileName, String packageName, String label,
                             String versionName, long versionCode, String client,
                             ApkType type, ApkStatus status, String cloudPath) {
        return new Apk(UUID.randomUUID().toString(), apkFileName, packageName, label,
                versionName, versionCode, client, type, status, cloudPath);
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    public String id() { return id; }
    public String apkFileName() { return apkFileName; }
    public String packageName() { return packageName; }
    public String label() { return label; }
    public String versionName() { return versionName; }
    public long versionCode() { return versionCode; }
    public String client() { return client; }
    public ApkType type() { return type; }
    public ApkStatus status() { return status; }
    public String cloudPath() { return cloudPath; }
}
