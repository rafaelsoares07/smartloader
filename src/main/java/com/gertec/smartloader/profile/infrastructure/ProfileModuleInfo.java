package com.gertec.smartloader.profile.infrastructure;

import com.gertec.smartloader.profile.domain.ModuleInfo;

/**
 * Infrastructure implementation of the Profile {@link ModuleInfo} port.
 */
public class ProfileModuleInfo implements ModuleInfo {

    @Override
    public String message() {
        return "Você está no módulo Profile";
    }
}
