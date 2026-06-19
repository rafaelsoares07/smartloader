package com.gertec.smartloader.smarthub.application;

import com.gertec.smartloader.smarthub.domain.ModuleInfo;

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
