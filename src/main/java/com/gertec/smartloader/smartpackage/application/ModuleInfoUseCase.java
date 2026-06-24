package com.gertec.smartloader.smartpackage.application;

import com.gertec.smartloader.smartpackage.domain.ModuleInfo;


public class ModuleInfoUseCase {

    private final ModuleInfo moduleInfo;

    public ModuleInfoUseCase(ModuleInfo moduleInfo) {
        this.moduleInfo = moduleInfo;
    }

    public String message() {
        return moduleInfo.message();
    }
}
