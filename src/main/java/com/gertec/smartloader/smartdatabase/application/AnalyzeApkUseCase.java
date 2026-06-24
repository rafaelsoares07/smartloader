package com.gertec.smartloader.smartdatabase.application;


public class AnalyzeApkUseCase {

    private final ApkAnalyzer apkAnalyzer;

    public AnalyzeApkUseCase(ApkAnalyzer apkAnalyzer) {
        this.apkAnalyzer = apkAnalyzer;
    }

    public ApkAnalyzer.ApkMetadata execute(String apkPath) {
        return apkAnalyzer.analyze(apkPath);
    }
}
