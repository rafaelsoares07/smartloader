package com.gertec.smartloader.smartdatabase.application;

import com.gertec.smartloader.smartdatabase.domain.entity.SigningProfile;
import com.gertec.smartloader.smartdatabase.domain.repository.SigningProfileRepository;

import java.util.List;

public class ListSigningProfilesUseCase {
    private final SigningProfileRepository repository;
    public ListSigningProfilesUseCase(SigningProfileRepository repository) { this.repository = repository; }
    public List<SigningProfile> execute() { return repository.findAll(); }
}
