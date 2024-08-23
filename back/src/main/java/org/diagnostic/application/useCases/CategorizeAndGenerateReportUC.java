package org.diagnostic.application.useCases;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.diagnostic.domain.entities.Tomography;
import org.diagnostic.infraestructure.repositories.interfaces.ITomographyRepository;
import org.diagnostic.presentation.responseModels.PredictionResponse;

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
        String apiUrl = "http://127.0.0.1:5000/predicted";
        Tomography.TomographyCategory tomographyCategory = null;
        try {
            byte[] tomographyBytes = tomography.getTomography();
            MultipartBodyBuilder builder = new MultipartBodyBuilder();
            builder.part("file", tomographyBytes).header("Content-Disposition", "form-data; name=file; filename=tomography.jpg");

            HttpEntity<MultiValueMap<String, HttpEntity<?>>> requestEntity = new HttpEntity<>(builder.build(), createHeaders());

            ResponseEntity<PredictionResponse> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, PredictionResponse.class);
            log.info("Categorizando la tomografia {}, api:{} - Respuesta: {}", tomography.getCodeReport(), apiUrl, response.getBody().toString());
            tomographyCategory = Tomography.TomographyCategory.valueOf(response.getBody().getPredictedClass().toUpperCase());
        } catch (Exception e) {
            log.error("Error al buscar la categoria de la tomografia {}", tomography.getCodeReport(), e);
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


    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return headers;
    }


}
