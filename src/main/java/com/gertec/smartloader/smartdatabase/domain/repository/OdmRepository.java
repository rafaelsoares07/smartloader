package com.gertec.smartloader.smartdatabase.domain.repository;



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
