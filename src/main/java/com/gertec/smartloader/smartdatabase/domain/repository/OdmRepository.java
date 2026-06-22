package com.gertec.smartloader.smartdatabase.domain.repository;

// O domínio define o contrato (salvar, atualizar, remover, consultar) — não COMO isso é feito.
// A entidade Odm não conhece esta interface.

import com.gertec.smartloader.smartdatabase.domain.entity.Odm;

import java.util.List;
import java.util.Optional;

public interface OdmRepository {

    void save(Odm odm);
    void update(Odm odm);
    Optional<Odm> findById(String id);
    List<Odm> findAll();
    void deleteById(String id);
    boolean existsByName(String name);
}
