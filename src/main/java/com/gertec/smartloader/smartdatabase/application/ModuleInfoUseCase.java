package com.gertec.smartloader.smartdatabase.application;

import com.gertec.smartloader.smartdatabase.domain.ModuleInfo;


public class ModuleInfoUseCase {

    private final ModuleInfo moduleInfo;

    public ModuleInfoUseCase(ModuleInfo moduleInfo) {
        this.moduleInfo = moduleInfo;
    }

    public String message() {
        return moduleInfo.message();
    }
}
