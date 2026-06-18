package com.gertec.smartloader.profile.infrastructure;

import com.gertec.smartloader.profile.application.ModuleInfoUseCase;
import com.gertec.smartloader.profile.domain.ModuleInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring wiring for the Profile feature. Beans are named per feature to avoid
 * type ambiguity in the single bootstrap application context.
 */
@Configuration
public class ProfileConfiguration {

    @Bean
    public ModuleInfo profileModuleInfo() {
        return new ProfileModuleInfo();
    }

    @Bean
    public ModuleInfoUseCase profileModuleInfoUseCase(ModuleInfo profileModuleInfo) {
        return new ModuleInfoUseCase(profileModuleInfo);
    }
}
