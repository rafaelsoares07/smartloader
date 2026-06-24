package com.gertec.smartloader.smartpackage.infrastructure;

import com.gertec.smartloader.smartpackage.application.ModuleInfoUseCase;
import com.gertec.smartloader.smartpackage.domain.ModuleInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


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
