package org.tptacs.application.useCases;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.tptacs.domain.entities.Tomography;
import org.tptacs.infraestructure.repositories.interfaces.ITomographyRepository;

@Service
@Slf4j
public class CategorizeAndGenerateReportUC {

    @Autowired
    private final RestTemplate restTemplate;

    private final ITomographyRepository tomographyRepository;

    public CategorizeAndGenerateReportUC(RestTemplate restTemplate, ITomographyRepository tomographyRepository) {
        this.restTemplate = restTemplate;
        this.tomographyRepository = tomographyRepository;
    }

    @Async
    public void categorizeAndGenerateReport(Tomography tomography){
        tomography.setStatusReport(Tomography.StatusReport.GENERACION_EN_PROCESO);
        Tomography.TomographyCategory tomographyCategory = categorizeTomography(tomography);

        if(tomographyCategory != null) {
            log.info("Tomografia {} categorizada correctamente: {}",tomography.getCodeReport(), tomographyCategory);
            tomography.setCategory(tomographyCategory);
            tomographyRepository.save(tomography);
        }

        String report = findReport(tomography);
        if (report != null) {
            tomography.setStatusReport(Tomography.StatusReport.INFORME_GENERADO);
            tomography.setReport(report);
            tomographyRepository.save(tomography);
        }

    }



    private Tomography.TomographyCategory categorizeTomography(Tomography tomography) {
        String apiUrl = "https://539f-181-229-20-5.ngrok-free.app/Input";
        Tomography.TomographyCategory tomographyCategory = null;
        try{
            String respone = restTemplate.postForObject(apiUrl, tomography.getTomography(), String.class);
            log.info("Categorizando la tomografia {}, api:{} - Respuesta: {}",tomography.getCodeReport(), apiUrl, respone);
            tomographyCategory = Tomography.TomographyCategory.valueOf(respone);
        }catch (Exception e){
            log.error("Error al buscar la categoria de la tomografia {}",tomography.getCodeReport());
        }
        return tomographyCategory;
    }

    private String findReport(Tomography tomography) {
        String apiUrl = "http://134.122.16.32:11434/api/generate";
        restTemplate.postForObject(apiUrl, "{ \"model\": \"d-iag-model\", \"prompt\":" + tomography.getCategory().name() + " , \"stream\": false }", String.class);

        String response = null;
        try{
            String respone = restTemplate.postForObject(apiUrl, "{ \"model\": \"d-iag-model\", \"prompt\":" + tomography.getCategory().name() + " , \"stream\": false }", String.class);
            log.info("Generando informe para la tomografia {}, api:{} - Respuesta: {}", tomography.getCodeReport(), apiUrl, respone);
        }catch (Exception e){
            log.error("Error al buscar la categoria de la tomografia {}",tomography.getCodeReport());
        }
        return response;
    }


}
