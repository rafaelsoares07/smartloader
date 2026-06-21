package com.gertec.smartloader.smartdatabase.domain.enums;

// Situação do perfil de assinatura.
// PENDENTE: cadastrado, mas ainda não validado contra o keystore real.
// ATIVA: keystore + alias + senhas validados com sucesso.
// INVALIDA: a validação falhou (arquivo, senha ou alias incorretos).
public enum SigningProfileStatus {
    ATIVA,
    INATIVA,
    PENDENTE,
    INVALIDA,
}
