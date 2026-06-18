package com.gertec.smartloader.recording.application;

import com.gertec.smartloader.recording.domain.ModuleInfo;

/**
 * Application use case for the Recording feature. Plain POJO with NO framework annotation.
 */
public class ModuleInfoUseCase {

    private final ModuleInfo moduleInfo;

    public ModuleInfoUseCase(ModuleInfo moduleInfo) {
        this.moduleInfo = moduleInfo;
    }

    public String message() {
        return moduleInfo.message();
    }
}
