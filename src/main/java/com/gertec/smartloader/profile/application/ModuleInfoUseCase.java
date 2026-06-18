package com.gertec.smartloader.profile.application;

import com.gertec.smartloader.profile.domain.ModuleInfo;

/**
 * Application use case for the Profile feature. Plain POJO with NO framework annotation.
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
