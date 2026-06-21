package com.gertec.smartloader.smartdatabase.application;

/**
 * Testa um conjunto de credenciais de assinatura (sem persistir nada): usado pelo botão
 * "TESTAR CHAVE" do formulário para conferir keystore + alias + senhas antes de cadastrar.
 *
 * <p>Camada de application: sem JavaFX nem Spring. Delega a verificação real ao
 * {@link KeystoreValidator}. As mensagens devolvidas nunca contêm senhas.</p>
 */
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
