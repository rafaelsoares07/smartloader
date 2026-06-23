package com.gertec.smartloader.smartdatabase.domain.repository;

// O domínio define o contrato (salvar, atualizar, remover, consultar) — não COMO isso é feito.
// A entidade Client não conhece esta interface.

import com.gertec.smartloader.smartdatabase.domain.entity.Client;

import java.util.List;
import java.util.Optional;

public interface ClientRepository {

    void save(Client client);
    void update(Client client);
    Optional<Client> findById(String id);
    List<Client> findAll();
    void deleteById(String id);
    boolean existsByName(String name);
}
