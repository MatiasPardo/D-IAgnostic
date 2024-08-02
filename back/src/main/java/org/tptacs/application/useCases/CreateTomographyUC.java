package org.tptacs.application.useCases;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.tptacs.domain.entities.Tomography;
import org.tptacs.infraestructure.repositories.interfaces.ITomographyRepository;

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

    public Tomography getTomographyStatus(String codeReport, String userId) {
        return tomographyRepository.findByCodeReport(codeReport);
    }
}
