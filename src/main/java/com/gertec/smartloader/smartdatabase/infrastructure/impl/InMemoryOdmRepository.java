package com.gertec.smartloader.smartdatabase.infrastructure.impl;

import com.gertec.smartloader.smartdatabase.domain.entity.Odm;
import com.gertec.smartloader.smartdatabase.domain.repository.OdmRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryOdmRepository implements OdmRepository {

    private final Map<String, Odm> data = new ConcurrentHashMap<>();

    @Override public void save(Odm odm) { data.put(odm.id(), odm); }
    @Override public void update(Odm odm) { data.put(odm.id(), odm); }
    @Override public Optional<Odm> findById(String id) { return Optional.ofNullable(data.get(id)); }
    @Override public List<Odm> findAll() { return new ArrayList<>(data.values()); }
    @Override public void deleteById(String id) { data.remove(id); }

    @Override
    public boolean existsByName(String name) {
        if (name == null) return false;
        return data.values().stream().anyMatch(o -> o.name().equalsIgnoreCase(name.trim()));
    }
}
