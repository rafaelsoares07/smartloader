package com.gertec.smartloader.recording.infrastructure;

import com.gertec.smartloader.recording.application.ModuleInfoUseCase;
import com.gertec.smartloader.recording.domain.ModuleInfo;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Spring wiring for the Recording feature. Beans are named per feature to avoid
 * type ambiguity in the single bootstrap application context.
 */
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
