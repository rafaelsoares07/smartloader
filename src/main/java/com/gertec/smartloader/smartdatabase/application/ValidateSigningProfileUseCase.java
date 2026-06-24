package com.gertec.smartloader.smartdatabase.application;


public class ValidateSigningProfileUseCase {

    private final KeystoreValidator keystoreValidator;

    public ValidateSigningProfileUseCase(KeystoreValidator keystoreValidator) {
        this.keystoreValidator = keystoreValidator;
    }

    public record Input(String keystorePath, String keyAlias,
                        String keystorePassword, String keyPassword) {}

    public KeystoreValidator.Result execute(Input input) {
        return keystoreValidator.validate(
                input.keystorePath(), input.keyAlias(),
                input.keystorePassword(), input.keyPassword());
    }
}
