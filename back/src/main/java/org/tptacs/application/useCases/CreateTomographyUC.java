package org.tptacs.application.useCases;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.tptacs.domain.entities.Tomography;
import org.tptacs.infraestructure.repositories.interfaces.ITomographyRepository;

import java.util.List;
import java.util.UUID;

@Service
public class CreateTomographyUC {


    private final ITomographyRepository tomographyRepository;


    public CreateTomographyUC(ITomographyRepository tomographyRepository) {
        this.tomographyRepository = tomographyRepository;
    }

    public String saveTomography(Tomography tomography) {
        // Generate unique code
        String codeReport = UUID.randomUUID().toString().substring(0, 8);
        tomography.setCodeReport(codeReport);
        tomography.setStatusReport(Tomography.StatusReport.SIN_INFORME);
        tomography.setCategory(Tomography.TomographyCategory.STATELESS);

        // Save Tomography
        tomographyRepository.save(tomography);

        return codeReport;
    }

    public Tomography getTomographyStatus(String codeReport) {
        return tomographyRepository.findByCodeReport(codeReport);
    }

    public List<Tomography> getTomography(String userId) {
        return tomographyRepository.findByUserId(userId);
    }

    public Page<Tomography> getTomography(String userId, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return tomographyRepository.findByUserId(userId, pageable);

    }
}

