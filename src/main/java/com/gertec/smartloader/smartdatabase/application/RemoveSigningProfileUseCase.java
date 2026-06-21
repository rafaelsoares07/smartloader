package com.gertec.smartloader.smartdatabase.application;

import com.gertec.smartloader.smartdatabase.domain.repository.SigningProfileRepository;

public class RemoveSigningProfileUseCase {
    private final SigningProfileRepository repository;
    public RemoveSigningProfileUseCase(SigningProfileRepository repository) { this.repository = repository; }
    public void execute(String id) { repository.removeById(id); }
}
