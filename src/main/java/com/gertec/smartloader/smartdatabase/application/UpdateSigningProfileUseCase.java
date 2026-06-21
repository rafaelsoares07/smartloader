package com.gertec.smartloader.smartdatabase.application;

import com.gertec.smartloader.smartdatabase.domain.entity.SigningProfile;
import com.gertec.smartloader.smartdatabase.domain.enums.SigningProfileStatus;
import com.gertec.smartloader.smartdatabase.domain.enums.SigningProfileType;
import com.gertec.smartloader.smartdatabase.domain.repository.SigningProfileRepository;

public class UpdateSigningProfileUseCase {

    private final SigningProfileRepository repository;
    private final KeystoreValidator keystoreValidator;

    public UpdateSigningProfileUseCase(SigningProfileRepository repository,
                                       KeystoreValidator keystoreValidator) {
        this.repository = repository;
        this.keystoreValidator = keystoreValidator;
    }

    public record Input(String id, String name, String keystorePath, String keyAlias,
                        String keystorePassword, String keyPassword,
                        SigningProfileType type, SigningProfileStatus status, String note) {}

    public SigningProfile execute(Input input) {
        SigningProfile existing = repository.findById(input.id())
                .orElseThrow(() -> new IllegalArgumentException("assinatura não encontrada para edição"));

        SigningProfileValidation.validateRequiredFields(
                input.name(), input.keyAlias(), input.keystorePath(),
                input.keystorePassword(), input.keyPassword());

        // Só bloqueia duplicidade se o nome realmente mudou para um já existente.
        String newName = input.name().trim();
        if (!existing.name().equalsIgnoreCase(newName) && repository.existsByName(newName))
            throw new IllegalArgumentException("já existe uma assinatura com este nome");

        // Mesma regra do cadastro: a chave precisa ser válida para salvar a edição.
        KeystoreValidator.Result validation = keystoreValidator.validate(
                input.keystorePath(), input.keyAlias(),
                input.keystorePassword(), input.keyPassword());
        if (!validation.valid())
            throw new IllegalArgumentException("Chave inválida: " + validation.message());

        // Chave validada: mantém o status escolhido, ou ATIVA quando não informado.
        SigningProfileStatus status = input.status() == null ? SigningProfileStatus.ATIVA : input.status();

        // Reconstrói mantendo o id e a data de criação; updatedAt é renovado pelo construtor de domínio.
        SigningProfile updated = new SigningProfile(
                existing.id(), newName, input.keystorePath().trim(), input.keyAlias().trim(),
                input.keystorePassword(), input.keyPassword(), input.type(), status, input.note(),
                existing.createdAt(), java.time.LocalDateTime.now());
        repository.update(updated);
        return updated;
    }
}
