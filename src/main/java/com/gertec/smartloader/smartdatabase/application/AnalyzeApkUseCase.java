package com.gertec.smartloader.smartdatabase.application;

/**
 * Lê os metadados de um arquivo .apk (sem persistir nada): usado pelo cadastro para
 * preencher o formulário automaticamente a partir do arquivo escolhido.
 *
 * <p>Camada de application: sem JavaFX nem Spring. Delega a leitura real ao {@link ApkAnalyzer}.</p>
 */
public class AnalyzeApkUseCase {

    private final ApkAnalyzer apkAnalyzer;

    public AnalyzeApkUseCase(ApkAnalyzer apkAnalyzer) {
        this.apkAnalyzer = apkAnalyzer;
    }

    /** @throws ApkAnalysisException quando o arquivo não é um APK legível. */
    public ApkAnalyzer.ApkMetadata execute(String apkPath) {
        return apkAnalyzer.analyze(apkPath);
    }
}
