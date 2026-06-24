package com.gertec.smartloader.smartdatabase.infrastructure;

import com.gertec.smartloader.smartdatabase.application.CreateClientUseCase;
import com.gertec.smartloader.smartdatabase.application.ListClientUseCase;
import com.gertec.smartloader.smartdatabase.application.RemoveClientUseCase;
import com.gertec.smartloader.smartdatabase.application.UpdateClientUseCase;
import com.gertec.smartloader.smartdatabase.domain.repository.ClientRepository;
import com.gertec.smartloader.smartdatabase.infrastructure.impl.InMemoryClientRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {

    @Bean
    public ClientRepository clientRepository() {
        return new InMemoryClientRepository();
    }

    @Bean
    public CreateClientUseCase createClientUseCase(ClientRepository clientRepository) {
        return new CreateClientUseCase(clientRepository);
    }

    @Bean
    public ListClientUseCase listClientUseCase(ClientRepository clientRepository) {
        return new ListClientUseCase(clientRepository);
    }

    @Bean
    public UpdateClientUseCase updateClientUseCase(ClientRepository clientRepository) {
        return new UpdateClientUseCase(clientRepository);
    }

    @Bean
    public RemoveClientUseCase removeClientUseCase(ClientRepository clientRepository) {
        return new RemoveClientUseCase(clientRepository);
    }
}
