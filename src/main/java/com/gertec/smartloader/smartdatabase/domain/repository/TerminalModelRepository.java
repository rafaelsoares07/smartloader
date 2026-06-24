package com.gertec.smartloader.smartdatabase.domain.repository;

import com.gertec.smartloader.smartdatabase.domain.entity.TerminalModel;

import java.util.List;
import java.util.Optional;

public interface TerminalModelRepository {

    void save(TerminalModel terminalModel);
    Optional<TerminalModel> findById(String id);
    List<TerminalModel> findAll();
    void deleteById(String id);
}
