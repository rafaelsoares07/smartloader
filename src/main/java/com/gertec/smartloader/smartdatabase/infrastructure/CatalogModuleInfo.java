package com.gertec.smartloader.smartdatabase.infrastructure;

import com.gertec.smartloader.smartdatabase.domain.ModuleInfo;

public class CatalogModuleInfo implements ModuleInfo {

    @Override
    public String message() {
        return "Você está no módulo Catalog";
    }
}
