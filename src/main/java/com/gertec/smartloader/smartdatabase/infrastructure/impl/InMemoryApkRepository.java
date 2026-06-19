package com.gertec.smartloader.smartdatabase.infrastructure.impl;

import com.gertec.smartloader.smartdatabase.domain.entity.Apk;
import com.gertec.smartloader.smartdatabase.domain.repository.ApkRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryApkRepository implements ApkRepository {

    private final Map<String, Apk> data = new ConcurrentHashMap<>();

    @Override public void save(Apk apk) { data.put(apk.id(), apk); }
    @Override public Optional<Apk> findById(String id) { return Optional.ofNullable(data.get(id)); }
    @Override public List<Apk> findAll() { return new ArrayList<>(data.values()); }
    @Override public void deleteById(String id) { data.remove(id); }
}
