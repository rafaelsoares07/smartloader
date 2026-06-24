package com.gertec.smartloader.smartdatabase.application;

import com.gertec.smartloader.smartdatabase.domain.entity.Odm;
import com.gertec.smartloader.smartdatabase.domain.repository.OdmRepository;
import com.gertec.smartloader.smartdatabase.domain.repository.SigningProfileRepository;

public class AssignSignatureToOdmUseCase {

    private final OdmRepository odmRepository;
    private final SigningProfileRepository signingProfileRepository;

    public AssignSignatureToOdmUseCase(OdmRepository odmRepository,
                                       SigningProfileRepository signingProfileRepository) {
        this.odmRepository = odmRepository;
        this.signingProfileRepository = signingProfileRepository;
    }

    public record Input(String odmId, String signatureId) {}

    public Odm execute(Input input) {
        Odm odm = odmRepository.findById(input.odmId())
                .orElseThrow(() -> new IllegalArgumentException("ODM não encontrada"));

        if (input.signatureId() == null || input.signatureId().isBlank())
            throw new IllegalArgumentException("assinatura é obrigatória para o vínculo");

        // A assinatura precisa existir para poder ser vinculada à ODM.
        if (signingProfileRepository.findById(input.signatureId()).isEmpty())
            throw new IllegalArgumentException("assinatura não encontrada");

        Odm linked = odm.withSignature(input.signatureId());
        odmRepository.update(linked);
        return linked;
    }
}
