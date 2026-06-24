package com.gertec.smartloader.smartdatabase.domain.entity;

import com.gertec.smartloader.smartdatabase.domain.enums.ApkStatus;
import com.gertec.smartloader.smartdatabase.domain.enums.ApkType;

import java.util.UUID;


public final class Apk {

    private final String id;
    private final String apkFileName;
    private final String packageName;
    private final String label;
    private final String versionName;
    private final long versionCode;
    private final String clientId;
    private final ApkType type;
    private final ApkStatus status;
    private final String cloudPath;
    private final boolean principal;

    public Apk(String id, String apkFileName, String packageName, String label,
               String versionName, long versionCode, String clientId,
               ApkType type, ApkStatus status, String cloudPath, boolean principal) {
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
        this.clientId = nullToEmpty(clientId);
        this.type = type;
        this.status = status;
        this.cloudPath = nullToEmpty(cloudPath);
        this.principal = principal;
    }


    public static Apk create(String apkFileName, String packageName, String label,
                             String versionName, long versionCode, String clientId,
                             ApkType type, ApkStatus status, String cloudPath) {
        return new Apk(UUID.randomUUID().toString(), apkFileName, packageName, label,
                versionName, versionCode, clientId, type, status, cloudPath, false);
    }

    public Apk withPrincipal(boolean newPrincipal) {
        return new Apk(id, apkFileName, packageName, label, versionName, versionCode,
                clientId, type, status, cloudPath, newPrincipal);
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
    public String clientId() { return clientId; }
    public ApkType type() { return type; }
    public ApkStatus status() { return status; }
    public String cloudPath() { return cloudPath; }
    public boolean principal() { return principal; }
}
