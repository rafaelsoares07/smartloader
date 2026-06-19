package com.gertec.smartloader.smartdatabase.domain.repository;


// Perceba que a interface conhece a entity, mas nossa entity "Apk" não conhece essa interface
// O dóminio define o contrato que seria oque deve ser feito (salvar, deletar), mas não como fazer isso!!!

import com.gertec.smartloader.smartdatabase.domain.entity.Apk;
import java.util.List;
import java.util.Optional;

public interface ApkRepository {

    void save(Apk apk);
    Optional<Apk> findById(String id);
    List<Apk> findAll();
    void deleteById(String id);
}
