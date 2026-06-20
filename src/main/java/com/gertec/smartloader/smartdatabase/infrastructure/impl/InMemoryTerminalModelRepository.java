package com.gertec.smartloader.smartdatabase.infrastructure.impl;

import com.gertec.smartloader.smartdatabase.domain.entity.Apk;
import com.gertec.smartloader.smartdatabase.domain.entity.TerminalModel;
import com.gertec.smartloader.smartdatabase.domain.repository.ApkRepository;
import com.gertec.smartloader.smartdatabase.domain.repository.TerminalModelRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTerminalModelRepository implements TerminalModelRepository {

    private final Map<String, TerminalModel> data = new ConcurrentHashMap<>();

    @Override
    public void save(TerminalModel terminalModel) {data.put(terminalModel.id(), terminalModel);}
    @Override public Optional<TerminalModel> findById(String id) { return Optional.ofNullable(data.get(id)); }
    @Override public List<TerminalModel> findAll() { return new ArrayList<>(data.values()); }
    @Override public void deleteById(String id) { data.remove(id); }
}
