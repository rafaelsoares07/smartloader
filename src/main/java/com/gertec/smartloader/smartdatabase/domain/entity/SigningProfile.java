package com.gertec.smartloader.smartdatabase.domain.entity;

import com.gertec.smartloader.smartdatabase.domain.enums.SigningProfileStatus;
import com.gertec.smartloader.smartdatabase.domain.enums.SigningProfileType;

import java.time.LocalDateTime;
import java.util.UUID;

// Entidade pura: apenas Java, sem JavaFX nem Spring. As validações de invariante vivem aqui.
//
// ATENÇÃO — dados sensíveis:
// keystorePassword e keyPassword são segredos. Eles ficam apenas em memória (impl InMemory).
// O toString() NUNCA expõe as senhas reais; qualquer log/Alert/DTO deve respeitar isso.
public final class SigningProfile {

    // Máscara fixa usada em toString para nunca revelar tamanho/conteúdo das senhas.
    private static final String SECRET_MASK = "****";

    private final String id;
    private final String name;
    private final String keystorePath;        // caminho/referência do arquivo .jks ou .keystore
    private final String keyAlias;
    private final String keystorePassword;     // sensível — não logar, não exibir
    private final String keyPassword;          // sensível — não logar, não exibir
    private final SigningProfileType type;
    private final SigningProfileStatus status;
    private final String note;                 // observação opcional
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;

    // Construtor de reconstrução: recria um perfil já existente (persistência / edição) mantendo o id.
    public SigningProfile(String id, String name, String keystorePath, String keyAlias,
                          String keystorePassword, String keyPassword,
                          SigningProfileType type, SigningProfileStatus status, String note,
                          LocalDateTime createdAt, LocalDateTime updatedAt) {
        if (id == null || id.isBlank())
            throw new IllegalArgumentException("id é obrigatório");
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("nome da assinatura é obrigatório");
        if (keystorePath == null || keystorePath.isBlank())
            throw new IllegalArgumentException("arquivo do keystore é obrigatório");
        if (keyAlias == null || keyAlias.isBlank())
            throw new IllegalArgumentException("alias da chave é obrigatório");
        if (keystorePassword == null || keystorePassword.isEmpty())
            throw new IllegalArgumentException("senha do keystore é obrigatória");
        if (keyPassword == null || keyPassword.isEmpty())
            throw new IllegalArgumentException("senha da chave é obrigatória");
        if (type == null)
            throw new IllegalArgumentException("tipo da assinatura é obrigatório");
        if (status == null)
            throw new IllegalArgumentException("status da assinatura é obrigatório");
        if (createdAt == null)
            throw new IllegalArgumentException("data de criação é obrigatória");
        if (updatedAt == null)
            throw new IllegalArgumentException("data de atualização é obrigatória");

        this.id = id;
        this.name = name;
        this.keystorePath = keystorePath;
        this.keyAlias = keyAlias;
        this.keystorePassword = keystorePassword;
        this.keyPassword = keyPassword;
        this.type = type;
        this.status = status;
        this.note = nullToEmpty(note);
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Fábrica para um perfil NOVO: o domínio gera o id e os timestamps.
    public static SigningProfile create(String name, String keystorePath, String keyAlias,
                                        String keystorePassword, String keyPassword,
                                        SigningProfileType type, SigningProfileStatus status, String note) {
        LocalDateTime now = LocalDateTime.now();
        return new SigningProfile(UUID.randomUUID().toString(), name, keystorePath, keyAlias,
                keystorePassword, keyPassword, type, status, note, now, now);
    }

    // Devolve uma cópia com novo status (e updatedAt renovado), preservando todo o restante.
    // Usado pela validação do keystore para marcar ATIVA / INVALIDA sem mutar a entidade original.
    public SigningProfile withStatus(SigningProfileStatus newStatus) {
        return new SigningProfile(id, name, keystorePath, keyAlias, keystorePassword, keyPassword,
                type, newStatus, note, createdAt, LocalDateTime.now());
    }

    private static String nullToEmpty(String value) {
        return value == null ? "" : value;
    }

    public String id() { return id; }
    public String name() { return name; }
    public String keystorePath() { return keystorePath; }
    public String keyAlias() { return keyAlias; }
    public String keystorePassword() { return keystorePassword; }
    public String keyPassword() { return keyPassword; }
    public SigningProfileType type() { return type; }
    public SigningProfileStatus status() { return status; }
    public String note() { return note; }
    public LocalDateTime createdAt() { return createdAt; }
    public LocalDateTime updatedAt() { return updatedAt; }

    // Nome do arquivo (sem o caminho) — conveniência para exibição na tabela.
    public String keystoreFileName() {
        if (keystorePath == null || keystorePath.isBlank()) return "";
        String normalized = keystorePath.replace('\\', '/');
        int slash = normalized.lastIndexOf('/');
        return slash >= 0 ? normalized.substring(slash + 1) : normalized;
    }

    // Importante: jamais incluir keystorePassword/keyPassword reais aqui.
    @Override
    public String toString() {
        return "SigningProfile{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", keystorePath='" + keystorePath + '\'' +
                ", keyAlias='" + keyAlias + '\'' +
                ", keystorePassword='" + SECRET_MASK + '\'' +
                ", keyPassword='" + SECRET_MASK + '\'' +
                ", type=" + type +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
