package com.gertec.smartloader.smartdatabase.infrastructure;

import com.gertec.smartloader.smartdatabase.application.CreateApkUseCase;
import com.gertec.smartloader.smartdatabase.application.ListApksUseCase;
import com.gertec.smartloader.smartdatabase.application.ModuleInfoUseCase;
import com.gertec.smartloader.smartdatabase.application.RemoveApkUseCase;
import com.gertec.smartloader.smartdatabase.domain.ModuleInfo;
import com.gertec.smartloader.smartdatabase.domain.repository.ApkRepository;
import com.gertec.smartloader.smartdatabase.domain.repository.TerminalModelRepository;
import com.gertec.smartloader.smartdatabase.infrastructure.impl.InMemoryApkRepository;
import com.gertec.smartloader.smartdatabase.infrastructure.impl.InMemoryTerminalModelRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TerminalModelConfiguration {

    // Daqui para baixo que importa, entender isso aqui já matou a arquitetura
    @Bean
    public TerminalModelRepository terminalModelRepository() {
        return new InMemoryTerminalModelRepository();
    }

    @Bean
    public CreateApkUseCase createApkUseCase(ApkRepository apkRepository) {return new CreateApkUseCase(apkRepository);}

    @Bean
    public ListApksUseCase listApksUseCase(ApkRepository apkRepository) {
        return new ListApksUseCase(apkRepository);
    }

    @Bean
    public RemoveApkUseCase removeApkUseCase(ApkRepository apkRepository) {return new RemoveApkUseCase(apkRepository);}
}
