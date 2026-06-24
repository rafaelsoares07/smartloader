package com.gertec.smartloader.smartdatabase.application;


public class ApkAnalysisException extends RuntimeException {

    public ApkAnalysisException(String message) {
        super(message);
    }

    public ApkAnalysisException(String message, Throwable cause) {
        super(message, cause);
    }
}
