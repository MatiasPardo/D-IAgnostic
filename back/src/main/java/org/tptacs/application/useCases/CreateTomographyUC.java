package org.tptacs.application.useCases;


import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.tptacs.domain.entities.Tomography;
import org.tptacs.infraestructure.repositories.interfaces.ITomographyRepository;

import java.util.UUID;

@Service
public class CreateTomographyUC {


    private final ITomographyRepository tomographyRepository;

    private final RestTemplate restTemplate = new RestTemplate();

    public CreateTomographyUC(ITomographyRepository tomographyRepository) {
        this.tomographyRepository = tomographyRepository;
    }

    public String saveTomography(Tomography tomography) {
        // Generate unique code
        String codeReport = UUID.randomUUID().toString().substring(0, 8);
        tomography.setCodeReport(codeReport);
        tomography.setStatus(Tomography.Status.SIN_INFORME);

        // Save Tomography
        tomographyRepository.save(tomography);

        // Call external APIs
        tomography.setStatus(Tomography.Status.GENERACION_EN_PROCESO);
        tomographyRepository.save(tomography);

        // API calls
        callExternalApi1(tomography);
        callExternalApi2();

        // Update status
        tomography.setStatus(Tomography.Status.INFORME_GENERADO);
        tomographyRepository.save(tomography);

        return codeReport;
    }

    private void callExternalApi1(Tomography tomography) {
        String apiUrl = "https://539f-181-229-20-5.ngrok-free.app/Input";
        restTemplate.postForObject(apiUrl, tomography.getTomography(), String.class);
    }

    private void callExternalApi2() {
        String apiUrl = "http://134.122.16.32:11434/api/generate";
        restTemplate.postForObject(apiUrl, "{ \"model\": \"d-iag-model\", \"prompt\": \"canta algo\", \"stream\": false }", String.class);
    }

    public Tomography getTomographyStatus(String codeReport, String userId) {
        return tomographyRepository.findByCodeReport(codeReport);
    }
}
