package com.gertec.smartloader.smartdatabase.infrastructure;

import com.gertec.smartloader.smartdatabase.application.AssignSignatureToOdmUseCase;
import com.gertec.smartloader.smartdatabase.application.CreateOdmUseCase;
import com.gertec.smartloader.smartdatabase.application.ListOdmUseCase;
import com.gertec.smartloader.smartdatabase.application.RemoveOdmUseCase;
import com.gertec.smartloader.smartdatabase.application.ResolveSignatureForTerminalUseCase;
import com.gertec.smartloader.smartdatabase.application.UpdateOdmUseCase;
import com.gertec.smartloader.smartdatabase.domain.repository.OdmRepository;
import com.gertec.smartloader.smartdatabase.domain.repository.SigningProfileRepository;
import com.gertec.smartloader.smartdatabase.domain.repository.TerminalModelRepository;
import com.gertec.smartloader.smartdatabase.infrastructure.impl.InMemoryOdmRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OdmConfiguration {

    @Bean
    public OdmRepository odmRepository() {
        return new InMemoryOdmRepository();
    }

    @Bean
    public CreateOdmUseCase createOdmUseCase(OdmRepository odmRepository) {
        return new CreateOdmUseCase(odmRepository);
    }

    @Bean
    public ListOdmUseCase listOdmUseCase(OdmRepository odmRepository) {
        return new ListOdmUseCase(odmRepository);
    }

    @Bean
    public UpdateOdmUseCase updateOdmUseCase(OdmRepository odmRepository) {
        return new UpdateOdmUseCase(odmRepository);
    }

    @Bean
    public RemoveOdmUseCase removeOdmUseCase(OdmRepository odmRepository) {
        return new RemoveOdmUseCase(odmRepository);
    }

    // Vincula ODM ↔ Signature: precisa também do repositório de assinaturas para validar a existência.
    @Bean
    public AssignSignatureToOdmUseCase assignSignatureToOdmUseCase(OdmRepository odmRepository,
                                                                   SigningProfileRepository signingProfileRepository) {
        return new AssignSignatureToOdmUseCase(odmRepository, signingProfileRepository);
    }

    // Resolve a assinatura de um terminal percorrendo terminal → ODM → assinatura.
    @Bean
    public ResolveSignatureForTerminalUseCase resolveSignatureForTerminalUseCase(
            TerminalModelRepository terminalModelRepository,
            OdmRepository odmRepository,
            SigningProfileRepository signingProfileRepository) {
        return new ResolveSignatureForTerminalUseCase(terminalModelRepository, odmRepository, signingProfileRepository);
    }
}
