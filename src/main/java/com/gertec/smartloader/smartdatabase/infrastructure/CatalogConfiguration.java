package com.gertec.smartloader.smartdatabase.infrastructure;

import com.gertec.smartloader.smartdatabase.application.AnalyzeApkUseCase;
import com.gertec.smartloader.smartdatabase.application.ApkAnalyzer;
import com.gertec.smartloader.smartdatabase.application.CreateApkUseCase;
import com.gertec.smartloader.smartdatabase.application.ListApksUseCase;
import com.gertec.smartloader.smartdatabase.application.ModuleInfoUseCase;
import com.gertec.smartloader.smartdatabase.application.RemoveApkUseCase;
import com.gertec.smartloader.smartdatabase.application.SetPrincipalApkUseCase;
import com.gertec.smartloader.smartdatabase.application.UpdateApkUseCase;
import com.gertec.smartloader.smartdatabase.domain.ModuleInfo;
import com.gertec.smartloader.smartdatabase.domain.repository.ApkRepository;
import com.gertec.smartloader.smartdatabase.infrastructure.impl.ApkParserAnalyzer;
import com.gertec.smartloader.smartdatabase.infrastructure.impl.InMemoryApkRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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

    // Daqui para baixo que importa, entender isso aqui já matou a arquitetura
    @Bean
    public ApkRepository apkRepository() {
        return new InMemoryApkRepository();
    }

    @Bean
    public CreateApkUseCase createApkUseCase(ApkRepository apkRepository) {
        return new CreateApkUseCase(apkRepository);
    }

    @Bean
    public ListApksUseCase listApksUseCase(ApkRepository apkRepository) {
        return new ListApksUseCase(apkRepository);
    }

    @Bean
    public RemoveApkUseCase removeApkUseCase(ApkRepository apkRepository) {
        return new RemoveApkUseCase(apkRepository);
    }

    @Bean
    public UpdateApkUseCase updateApkUseCase(ApkRepository apkRepository) {
        return new UpdateApkUseCase(apkRepository);
    }

    @Bean
    public SetPrincipalApkUseCase setPrincipalApkUseCase(ApkRepository apkRepository) {
        return new SetPrincipalApkUseCase(apkRepository);
    }

    // Leitor de metadados do APK: o adapter (infra) implementa o port (application).
    @Bean
    public ApkAnalyzer apkAnalyzer() {
        return new ApkParserAnalyzer();
    }

    @Bean
    public AnalyzeApkUseCase analyzeApkUseCase(ApkAnalyzer apkAnalyzer) {
        return new AnalyzeApkUseCase(apkAnalyzer);
    }
}
