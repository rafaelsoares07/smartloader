package com.gertec.smartloader.smartdatabase.application;

import com.gertec.smartloader.smartdatabase.domain.entity.Odm;
import com.gertec.smartloader.smartdatabase.domain.repository.OdmRepository;

import java.util.List;

public class ListOdmUseCase {

    private final OdmRepository repository;

    public ListOdmUseCase(OdmRepository repository) {
        this.repository = repository;
    }

    public List<Odm> execute() {
        return repository.findAll();
    }
}
