package com.gertec.smartloader.smartdatabase.domain.repository;



import com.gertec.smartloader.smartdatabase.domain.entity.SigningProfile;

import java.util.List;
import java.util.Optional;

public interface SigningProfileRepository {

    void save(SigningProfile signingProfile);
    void update(SigningProfile signingProfile);
    void removeById(String id);
    Optional<SigningProfile> findById(String id);
    List<SigningProfile> findAll();
    boolean existsByName(String name);
    boolean existsByAlias(String alias);
}
