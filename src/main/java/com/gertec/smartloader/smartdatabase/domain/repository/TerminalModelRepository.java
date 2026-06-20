package com.gertec.smartloader.smartdatabase.domain.repository;


// Perceba que a interface conhece a entity, mas nossa entity "TerminalModel" não conhece essa interface
// O dóminio define o contrato que seria oque deve ser feito (salvar, deletar), mas não como fazer isso!!!

import com.gertec.smartloader.smartdatabase.domain.entity.Apk;
import com.gertec.smartloader.smartdatabase.domain.entity.TerminalModel;

import java.util.List;
import java.util.Optional;

public interface TerminalModelRepository {

    void save(TerminalModel terminalModel);
    Optional<TerminalModel> findById(String id);
    List<TerminalModel> findAll();
    void deleteById(String id);
}
