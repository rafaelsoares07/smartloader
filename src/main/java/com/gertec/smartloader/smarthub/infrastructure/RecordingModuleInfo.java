package com.gertec.smartloader.smarthub.infrastructure;

import com.gertec.smartloader.smarthub.domain.ModuleInfo;


public class RecordingModuleInfo implements ModuleInfo {

    @Override
    public String message() {
        return "Você está no módulo Recording";
    }
}
