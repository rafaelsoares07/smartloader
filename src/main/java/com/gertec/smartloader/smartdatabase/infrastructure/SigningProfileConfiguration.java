package com.gertec.smartloader.smartdatabase.infrastructure;

import com.gertec.smartloader.smartdatabase.application.CreateSigningProfileUseCase;
import com.gertec.smartloader.smartdatabase.application.KeystoreValidator;
import com.gertec.smartloader.smartdatabase.application.ListSigningProfilesUseCase;
import com.gertec.smartloader.smartdatabase.application.RemoveSigningProfileUseCase;
import com.gertec.smartloader.smartdatabase.application.UpdateSigningProfileUseCase;
import com.gertec.smartloader.smartdatabase.application.ValidateSigningProfileUseCase;
import com.gertec.smartloader.smartdatabase.domain.repository.SigningProfileRepository;
import com.gertec.smartloader.smartdatabase.infrastructure.impl.InMemorySigningProfileRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SigningProfileConfiguration {

    @Bean
    public SigningProfileRepository signingProfileRepository() {
        return new InMemorySigningProfileRepository();
    }

    @Bean
    public KeystoreValidator keystoreValidator() {
        return new KeystoreValidator();
    }

    @Bean
    public CreateSigningProfileUseCase createSigningProfileUseCase(SigningProfileRepository signingProfileRepository,
                                                                   KeystoreValidator keystoreValidator) {
        return new CreateSigningProfileUseCase(signingProfileRepository, keystoreValidator);
    }

    @Bean
    public ListSigningProfilesUseCase listSigningProfilesUseCase(SigningProfileRepository signingProfileRepository) {
        return new ListSigningProfilesUseCase(signingProfileRepository);
    }

    @Bean
    public UpdateSigningProfileUseCase updateSigningProfileUseCase(SigningProfileRepository signingProfileRepository,
                                                                   KeystoreValidator keystoreValidator) {
        return new UpdateSigningProfileUseCase(signingProfileRepository, keystoreValidator);
    }

    @Bean
    public RemoveSigningProfileUseCase removeSigningProfileUseCase(SigningProfileRepository signingProfileRepository) {
        return new RemoveSigningProfileUseCase(signingProfileRepository);
    }

    @Bean
    public ValidateSigningProfileUseCase validateSigningProfileUseCase(KeystoreValidator keystoreValidator) {
        return new ValidateSigningProfileUseCase(keystoreValidator);
    }
}
