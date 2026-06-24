package com.gertec.smartloader.smarthub.application;

import com.gertec.smartloader.smarthub.domain.ModuleInfo;


public class ModuleInfoUseCase {

    private final ModuleInfo moduleInfo;

    public ModuleInfoUseCase(ModuleInfo moduleInfo) {
        this.moduleInfo = moduleInfo;
    }

    public String message() {
        return moduleInfo.message();
    }
}
