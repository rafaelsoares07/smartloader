package com.gertec.smartloader.smartdatabase.application;

import com.gertec.smartloader.smartdatabase.domain.entity.Apk;
import com.gertec.smartloader.smartdatabase.domain.enums.ApkType;

// Regra de negócio compartilhada entre criação e edição de APK:
// um app (mesmo packageName) é "selado" pelo tipo e cliente da sua primeira versão.
// Nenhuma versão posterior pode mudar o tipo (ex.: de GERTEC para CLIENTE) nem ser
// atribuída a um cliente diferente.
final class ApkTypeConsistency {

    private ApkTypeConsistency() {}

    static void assertSameTypeAndClient(Apk reference, ApkType newType, String newClientId) {
        if (reference.type() != newType) {
            throw new IllegalArgumentException(
                    "Este app já existe como tipo " + reference.type()
                            + " e não pode mudar de tipo em uma nova versão.");
        }
        if (!normalize(reference.clientId()).equals(normalize(newClientId))) {
            throw new IllegalArgumentException(
                    "Este app pertence " + describeOwner(reference.clientId())
                            + " e não pode ser atribuído a outro cliente.");
        }
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private static String describeOwner(String clientId) {
        return normalize(clientId).isBlank() ? "ao Padrão Gertec" : "a outro cliente";
    }
}
