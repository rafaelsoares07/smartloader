package com.gertec.smartloader.smartdatabase.application;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.Key;
import java.security.KeyStore;
import java.util.Locale;

/**
 * Valida credenciais de keystore Android contra o arquivo real, usando apenas {@link KeyStore}
 * do JDK: o arquivo existe, abre com a senha do keystore e o alias é recuperável com a senha
 * da chave.
 *
 * <p>Camada de application: sem dependência de JavaFX ou Spring. As mensagens devolvidas
 * NUNCA contêm senhas — são seguras para Alert/log.</p>
 */
public class KeystoreValidator {

    /** Resultado da validação — sem senhas, seguro para exibir. */
    public record Result(boolean valid, String message) {}

    public Result validate(String keystorePath, String keyAlias,
                           String keystorePassword, String keyPassword) {
        if (keystorePath == null || keystorePath.isBlank()) {
            return new Result(false, "Arquivo de keystore não informado.");
        }
        if (keyAlias == null || keyAlias.isBlank()) {
            return new Result(false, "Alias da chave não informado.");
        }
        if (keystorePassword == null || keystorePassword.isEmpty()) {
            return new Result(false, "Senha do keystore não informada.");
        }
        if (keyPassword == null || keyPassword.isEmpty()) {
            return new Result(false, "Senha da chave não informada.");
        }

        File file = new File(keystorePath);
        if (!file.exists() || !file.isFile()) {
            return new Result(false, "Arquivo de keystore não encontrado.");
        }

        String keystoreType = resolveKeystoreType(keystorePath);
        try (InputStream is = Files.newInputStream(file.toPath())) {
            KeyStore keyStore = KeyStore.getInstance(keystoreType);
            // Falha aqui normalmente significa senha do keystore incorreta ou arquivo corrompido.
            keyStore.load(is, keystorePassword.toCharArray());

            if (!keyStore.containsAlias(keyAlias)) {
                return new Result(false, "Alias informado não existe no keystore.");
            }

            // Recuperar a chave valida a senha da chave (lança UnrecoverableKeyException se errada).
            Key key = keyStore.getKey(keyAlias, keyPassword.toCharArray());
            if (key == null) {
                return new Result(false, "Não foi possível recuperar a chave para o alias informado.");
            }

            return new Result(true, "Keystore e alias validados com sucesso.");
        } catch (java.security.UnrecoverableKeyException e) {
            return new Result(false, "Senha da chave incorreta para o alias informado.");
        } catch (java.io.IOException e) {
            // KeyStore.load encapsula senha incorreta do keystore como IOException.
            return new Result(false, "Falha ao abrir o keystore: verifique o arquivo e a senha do keystore.");
        } catch (Exception e) {
            // Mensagem genérica — nunca incluir senha.
            return new Result(false, "Keystore inválido ou não suportado.");
        }
    }

    private String resolveKeystoreType(String path) {
        // .jks é sempre JKS; .keystore costuma ser JKS, mas pode ser PKCS12 — deixamos o JDK
        // escolher o default quando a extensão não é conclusiva.
        String lower = path.toLowerCase(Locale.ROOT);
        if (lower.endsWith(".jks")) {
            return "JKS";
        }
        return KeyStore.getDefaultType();
    }
}
