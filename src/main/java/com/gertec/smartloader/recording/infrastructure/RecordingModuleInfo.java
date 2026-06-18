package com.gertec.smartloader.recording.infrastructure;

import com.gertec.smartloader.recording.domain.ModuleInfo;

/**
 * Infrastructure implementation of the Recording {@link ModuleInfo} port.
 */
public class RecordingModuleInfo implements ModuleInfo {

    @Override
    public String message() {
        return "Você está no módulo Recording";
    }
}
