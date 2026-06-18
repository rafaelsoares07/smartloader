package com.gertec.smartloader.catalog.infrastructure;

import com.gertec.smartloader.catalog.domain.ModuleInfo;

/**
 * Infrastructure implementation of the Catalog {@link ModuleInfo} port.
 *
 * <p>The infrastructure layer may use frameworks and depends on the domain
 * (never the other way around). For Increment 0 it simply returns a fixed message
 * proving the vertical slice is wired end to end.</p>
 */
public class CatalogModuleInfo implements ModuleInfo {

    @Override
    public String message() {
        return "Você está no módulo Catalog";
    }
}
