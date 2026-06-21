package com.gertec.smartloader.smartdatabase.infrastructure.impl;

import com.gertec.smartloader.smartdatabase.domain.entity.SigningProfile;
import com.gertec.smartloader.smartdatabase.domain.repository.SigningProfileRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

// Implementação InMemory: as senhas permanecem apenas em memória de processo.
// Nada é gravado em arquivo/banco em texto puro por enquanto.
public class InMemorySigningProfileRepository implements SigningProfileRepository {

    private final Map<String, SigningProfile> data = new ConcurrentHashMap<>();

    @Override public void save(SigningProfile signingProfile) { data.put(signingProfile.id(), signingProfile); }
    @Override public void update(SigningProfile signingProfile) { data.put(signingProfile.id(), signingProfile); }
    @Override public void removeById(String id) { data.remove(id); }
    @Override public Optional<SigningProfile> findById(String id) { return Optional.ofNullable(data.get(id)); }
    @Override public List<SigningProfile> findAll() { return new ArrayList<>(data.values()); }

    @Override
    public boolean existsByName(String name) {
        if (name == null) return false;
        return data.values().stream().anyMatch(p -> p.name().equalsIgnoreCase(name.trim()));
    }

    @Override
    public boolean existsByAlias(String alias) {
        if (alias == null) return false;
        return data.values().stream().anyMatch(p -> p.keyAlias().equalsIgnoreCase(alias.trim()));
    }
}
