package com.gertec.smartloader.smartdatabase.application;

/**
 * Falha ao analisar um arquivo .apk (inexistente, corrompido ou não suportado).
 * A mensagem é segura para exibir em Alert — não contém caminhos sensíveis nem detalhes internos.
 */
public class ApkAnalysisException extends RuntimeException {

    public ApkAnalysisException(String message) {
        super(message);
    }

    public ApkAnalysisException(String message, Throwable cause) {
        super(message, cause);
    }
}
