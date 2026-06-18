package com.gertec.smartloader.catalog.application;

import com.gertec.smartloader.catalog.domain.ModuleInfo;

/**
 * Application use case for the Catalog feature.
 *
 * <p>Plain POJO with NO framework annotation. It receives the domain port through the
 * constructor and returns the module message. The Spring wiring of this POJO lives in
 * the infrastructure layer (a {@code @Configuration} that declares it as a bean).</p>
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
