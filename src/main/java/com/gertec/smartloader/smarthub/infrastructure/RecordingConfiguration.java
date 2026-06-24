package com.gertec.smartloader.smarthub.infrastructure;

import com.gertec.smartloader.smarthub.application.ModuleInfoUseCase;
import com.gertec.smartloader.smarthub.domain.ModuleInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RecordingConfiguration {

    @Bean
    public ModuleInfo recordingModuleInfo() {
        return new RecordingModuleInfo();
    }

    @Bean
    public ModuleInfoUseCase recordingModuleInfoUseCase(ModuleInfo recordingModuleInfo) {
        return new ModuleInfoUseCase(recordingModuleInfo);
    }
}
