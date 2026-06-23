package com.gertec.smartloader.smartdatabase.infrastructure.impl;

import com.gertec.smartloader.smartdatabase.application.ApkAnalysisException;
import com.gertec.smartloader.smartdatabase.application.ApkAnalyzer;
import net.dongliu.apk.parser.ApkFile;
import net.dongliu.apk.parser.bean.ApkMeta;

import java.io.File;
import java.io.IOException;

/**
 * Adapter que lê os metadados do APK com a biblioteca {@code net.dongliu:apk-parser}
 * (decodifica o AndroidManifest binário, sem precisar do aapt/Android SDK).
 *
 * <p>Toda dependência de terceiro fica confinada aqui, na infraestrutura — o port
 * {@link ApkAnalyzer} mantém o domínio/aplicação livres dela.</p>
 */
public class ApkParserAnalyzer implements ApkAnalyzer {

    @Override
    public ApkMetadata analyze(String apkPath) {
        if (apkPath == null || apkPath.isBlank()) {
            throw new ApkAnalysisException("Arquivo do APK não informado.");
        }
        File file = new File(apkPath);
        if (!file.isFile()) {
            throw new ApkAnalysisException("Arquivo do APK não encontrado.");
        }

        try (ApkFile apkFile = new ApkFile(file)) {
            ApkMeta meta = apkFile.getApkMeta();

            // label pode vir vazio (ou ser uma referência de recurso não resolvida);
            // nesse caso caímos para o "name" e, por fim, para o nome do pacote.
            String label = firstNonBlank(meta.getLabel(), meta.getName(), meta.getPackageName());
            long versionCode = meta.getVersionCode() != null ? meta.getVersionCode() : 0L;

            return new ApkMetadata(file.getName(), meta.getPackageName(), label,
                    meta.getVersionName(), versionCode);
        } catch (IOException e) {
            throw new ApkAnalysisException("Não foi possível ler o APK: arquivo inválido ou corrompido.", e);
        }
    }

    private static String firstNonBlank(String... values) {
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return "";
    }
}
