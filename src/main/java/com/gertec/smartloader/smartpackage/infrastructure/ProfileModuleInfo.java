package com.gertec.smartloader.smartpackage.infrastructure;

import com.gertec.smartloader.smartpackage.domain.ModuleInfo;

/**
 * Infrastructure implementation of the Profile {@link ModuleInfo} port.
 */
public class ProfileModuleInfo implements ModuleInfo {

    @Override
    public String message() {
        return "Você está no módulo Profile";
    }
}
