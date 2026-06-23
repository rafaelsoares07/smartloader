package com.gertec.smartloader.smartdatabase.application;

/**
 * Porta (contrato) para extrair metadados de um arquivo .apk apenas analisando-o.
 *
 * <p>Camada de application: define O QUE precisa ser lido (package, label, versão), nunca COMO.
 * A leitura real do AndroidManifest binário vive num adapter na infraestrutura — assim o
 * domínio/aplicação não conhece nenhuma biblioteca de terceiros.</p>
 */
public interface ApkAnalyzer {

    /** Metadados úteis lidos do APK (transporte simples, sem regra de negócio). */
    record ApkMetadata(String fileName, String packageName, String label,
                       String versionName, long versionCode) {}

    /**
     * Lê os metadados do APK no caminho informado.
     *
     * @throws ApkAnalysisException quando o arquivo não existe ou não é um APK legível.
     */
    ApkMetadata analyze(String apkPath);
}
