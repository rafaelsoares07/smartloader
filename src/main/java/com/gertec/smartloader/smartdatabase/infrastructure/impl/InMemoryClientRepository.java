package com.gertec.smartloader.smartdatabase.infrastructure.impl;

import com.gertec.smartloader.smartdatabase.domain.entity.Client;
import com.gertec.smartloader.smartdatabase.domain.repository.ClientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryClientRepository implements ClientRepository {

    private final Map<String, Client> data = new ConcurrentHashMap<>();

    @Override public void save(Client client) { data.put(client.id(), client); }
    @Override public void update(Client client) { data.put(client.id(), client); }
    @Override public Optional<Client> findById(String id) { return Optional.ofNullable(data.get(id)); }
    @Override public List<Client> findAll() { return new ArrayList<>(data.values()); }
    @Override public void deleteById(String id) { data.remove(id); }

    @Override
    public boolean existsByName(String name) {
        if (name == null) return false;
        return data.values().stream().anyMatch(c -> c.name().equalsIgnoreCase(name.trim()));
    }
}
