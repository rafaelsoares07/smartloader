package com.gertec.smartloader.smartdatabase.application;

import com.gertec.smartloader.smartdatabase.domain.entity.Odm;
import com.gertec.smartloader.smartdatabase.domain.entity.SigningProfile;
import com.gertec.smartloader.smartdatabase.domain.entity.TerminalModel;
import com.gertec.smartloader.smartdatabase.domain.repository.OdmRepository;
import com.gertec.smartloader.smartdatabase.domain.repository.SigningProfileRepository;
import com.gertec.smartloader.smartdatabase.domain.repository.TerminalModelRepository;

import java.util.Optional;


public class ResolveSignatureForTerminalUseCase {

    private final TerminalModelRepository terminalModelRepository;
    private final OdmRepository odmRepository;
    private final SigningProfileRepository signingProfileRepository;

    public ResolveSignatureForTerminalUseCase(TerminalModelRepository terminalModelRepository,
                                              OdmRepository odmRepository,
                                              SigningProfileRepository signingProfileRepository) {
        this.terminalModelRepository = terminalModelRepository;
        this.odmRepository = odmRepository;
        this.signingProfileRepository = signingProfileRepository;
    }

    public record Input(String terminalId) {}

    public Optional<SigningProfile> execute(Input input) {
        TerminalModel terminal = terminalModelRepository.findById(input.terminalId())
                .orElseThrow(() -> new IllegalArgumentException("Terminal não encontrado"));

        Optional<Odm> odm = odmRepository.findById(terminal.odmId());
        if (odm.isEmpty() || !odm.get().hasSignature())
            return Optional.empty();

        return signingProfileRepository.findById(odm.get().signatureId());
    }
}
