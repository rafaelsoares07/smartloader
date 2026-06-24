package com.gertec.smartloader.smartdatabase.application;

import java.util.Locale;

final class SigningProfileValidation {

    private SigningProfileValidation() {}

    static void validateRequiredFields(String name, String alias, String keystorePath,
                                       String keystorePassword, String keyPassword) {
        if (isBlank(name))
            throw new IllegalArgumentException("nome da assinatura é obrigatório");
        if (isBlank(alias))
            throw new IllegalArgumentException("alias da chave é obrigatório");
        if (isBlank(keystorePath))
            throw new IllegalArgumentException("arquivo do keystore é obrigatório");
        if (!hasKeystoreExtension(keystorePath))
            throw new IllegalArgumentException("arquivo deve terminar com .jks ou .keystore");
        if (keystorePassword == null || keystorePassword.isEmpty())
            throw new IllegalArgumentException("senha do keystore é obrigatória");
        if (keyPassword == null || keyPassword.isEmpty())
            throw new IllegalArgumentException("senha da chave é obrigatória");
    }

    static boolean hasKeystoreExtension(String path) {
        if (path == null) return false;
        String lower = path.trim().toLowerCase(Locale.ROOT);
        return lower.endsWith(".jks") || lower.endsWith(".keystore");
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}
