package com.gertec.smartloader.smartdatabase.application;


public interface ApkAnalyzer {

    record ApkMetadata(String fileName, String packageName, String label,
                       String versionName, long versionCode) {}

    ApkMetadata analyze(String apkPath);
}
