package com.gertec.smartloader.smartdatabase.infrastructure;

import com.gertec.smartloader.smartdatabase.application.CreateTerminalUseCase;
import com.gertec.smartloader.smartdatabase.application.ListTerminalUseCase;
import com.gertec.smartloader.smartdatabase.application.RemoveTerminalUseCase;
import com.gertec.smartloader.smartdatabase.application.UpdateTerminalUseCase;
import com.gertec.smartloader.smartdatabase.domain.repository.OdmRepository;
import com.gertec.smartloader.smartdatabase.domain.repository.TerminalModelRepository;
import com.gertec.smartloader.smartdatabase.infrastructure.impl.InMemoryTerminalModelRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TerminalModelConfiguration {

    // Daqui para baixo que importa: esta configuração cuida APENAS dos beans de terminal.
    @Bean
    public TerminalModelRepository terminalModelRepository() {
        return new InMemoryTerminalModelRepository();
    }

    @Bean
    public CreateTerminalUseCase createTerminalUseCase(TerminalModelRepository terminalModelRepository,
                                                       OdmRepository odmRepository) {
        return new CreateTerminalUseCase(terminalModelRepository, odmRepository);
    }

    @Bean
    public ListTerminalUseCase listTerminalUseCase(TerminalModelRepository terminalModelRepository) {
        return new ListTerminalUseCase(terminalModelRepository);
    }

    @Bean
    public RemoveTerminalUseCase removeTerminalUseCase(TerminalModelRepository terminalModelRepository) {
        return new RemoveTerminalUseCase(terminalModelRepository);
    }

    @Bean
    public UpdateTerminalUseCase updateTerminalUseCase(TerminalModelRepository terminalModelRepository) {
        return new UpdateTerminalUseCase(terminalModelRepository);
    }
}
