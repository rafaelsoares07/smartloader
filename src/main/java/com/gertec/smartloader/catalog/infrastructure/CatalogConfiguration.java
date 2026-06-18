package com.gertec.smartloader.catalog.infrastructure;

import com.gertec.smartloader.catalog.application.ModuleInfoUseCase;
import com.gertec.smartloader.catalog.domain.ModuleInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring wiring for the Catalog feature.
 *
 * <p>Beans are named per feature ({@code catalogModuleInfo}, {@code catalogModuleInfoUseCase})
 * so the three identical {@code ModuleInfo} / {@code ModuleInfoUseCase} types from the three
 * features can coexist in the single bootstrap application context without ambiguity.</p>
 */
@Configuration
public class CatalogConfiguration {

    @Bean
    public ModuleInfo catalogModuleInfo() {
        return new CatalogModuleInfo();
    }

    @Bean
    public ModuleInfoUseCase catalogModuleInfoUseCase(ModuleInfo catalogModuleInfo) {
        return new ModuleInfoUseCase(catalogModuleInfo);
    }
}
