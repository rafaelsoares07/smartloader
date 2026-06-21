package com.gertec.smartloader.smartdatabase.application;

import com.gertec.smartloader.smartdatabase.domain.entity.SigningProfile;
import com.gertec.smartloader.smartdatabase.domain.enums.SigningProfileStatus;
import com.gertec.smartloader.smartdatabase.domain.enums.SigningProfileType;
import com.gertec.smartloader.smartdatabase.domain.repository.SigningProfileRepository;

public class CreateSigningProfileUseCase {

    private final SigningProfileRepository repository;
    private final KeystoreValidator keystoreValidator;

    public CreateSigningProfileUseCase(SigningProfileRepository repository,
                                       KeystoreValidator keystoreValidator) {
        this.repository = repository;
        this.keystoreValidator = keystoreValidator;
    }

    public record Input(String name, String keystorePath, String keyAlias,
                        String keystorePassword, String keyPassword,
                        SigningProfileType type, SigningProfileStatus status, String note) {}

    public SigningProfile execute(Input input) {
        SigningProfileValidation.validateRequiredFields(
                input.name(), input.keyAlias(), input.keystorePath(),
                input.keystorePassword(), input.keyPassword());

        if (repository.existsByName(input.name().trim()))
            throw new IllegalArgumentException("já existe uma assinatura com este nome");

        // Validação real da chave no momento do cadastro: se for inconsistente, não adiciona.
        KeystoreValidator.Result validation = keystoreValidator.validate(
                input.keystorePath(), input.keyAlias(),
                input.keystorePassword(), input.keyPassword());
        if (!validation.valid())
            throw new IllegalArgumentException("Chave inválida: " + validation.message());

        // Chave validada com sucesso: nasce ATIVA (a menos que o usuário tenha escolhido outro status).
        SigningProfileStatus status = input.status() == null ? SigningProfileStatus.ATIVA : input.status();

        SigningProfile profile = SigningProfile.create(
                input.name().trim(), input.keystorePath().trim(), input.keyAlias().trim(),
                input.keystorePassword(), input.keyPassword(), input.type(), status, input.note());
        repository.save(profile);
        return profile;
    }
}
