package org.diagnostic.application.services;

import lombok.extern.slf4j.Slf4j;
import org.diagnostic.application.useCases.ImageCategorizationSummary;
import org.diagnostic.domain.entities.Tomography;
import org.diagnostic.domain.entities.TomographyCategory;
import org.diagnostic.domain.entities.TomographyDetail;
import org.diagnostic.presentation.requestModels.TomographyRequest;
import org.diagnostic.presentation.responseModels.PredictionResponse;
import org.diagnostic.presentation.responseModels.ReportIAResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class IARestClient {

    @Value("${ia.rest.client.category}")
    private String IARESTCLIENTCATEGORY;

    @Value("${ia.rest.client.report}")
    private String IARESTCLIENTREPORT;

    @Autowired
    private final RestTemplate restTemplate;

    private final ImageCategorizationSummary imageCategorizationSummary;

    public IARestClient(RestTemplate restTemplate, ImageCategorizationSummary imageCategorizationSummary) {
        this.restTemplate = restTemplate;
        this.imageCategorizationSummary = imageCategorizationSummary;
    }



    public ReportIAResponse findReport(Tomography tomography) {
        String apiUrl = IARESTCLIENTREPORT + "generate";
        ReportIAResponse response = null;
        try{

            String prompt = imageCategorizationSummary.summary(tomography.getTomographyDetail());
            log.info("Prompt para generar informe para tomografia con codeReport {}: {}",tomography.getCodeReport(), prompt);
            response = restTemplate.postForObject(
                    apiUrl, "{ " +
                            "\"model\": \"d-iag-model\", " +
                            "\"prompt\": \"" + prompt + "\", "+
                            "\"stream\": false " +
                            "}",
                     ReportIAResponse.class);


            //String respone = restTemplate.postForObject(apiUrl, "{ \"model\": \"d-iag-model\", \"prompt\":" + tomography.getCategory().name() + " , \"stream\": false }", String.class);
            log.info("Generando informe para la tomografia {}, api:{} - Respuesta: {}", tomography.getCodeReport(), apiUrl, response);
        }catch (Exception e){
            log.error("Error al buscar el informe de la tomografia {}",tomography.getCodeReport());
            log.error("Error: {}", e.getMessage());
        }
        return response;
    }

    public List<TomographyDetail> categorizeTomography(Tomography tomography) {
        String apiUrlpath = IARESTCLIENTCATEGORY + "/predicted";
        try {
            if(tomography.getTomographyDetail() == null){
                throw new RuntimeException("No se puede enviar la tomografia con codeReport: " + tomography.getCodeReport());
            }
            TomographyRequest requestBody = new TomographyRequest(tomography.getTomographyDetail().stream().map(TomographyDetail::getUrl).toList());

            HttpHeaders headers = createHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<TomographyRequest> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<PredictionResponse> response = restTemplate.exchange(apiUrlpath, HttpMethod.POST, requestEntity, PredictionResponse.class);
            log.info("Categorizando la tomografia {}, api:{} - Respuesta: {}", tomography.getCodeReport(), apiUrlpath, response.getBody().toString());
            List<TomographyDetail> tomographyDetails = new LinkedList<TomographyDetail>();
            response.getBody().getImages_by_class().forEach( (k,v) ->
                    v.forEach(url ->
                            tomographyDetails.add(
                                    new TomographyDetail(TomographyCategory.valueOf(k), url)
                            )
                    )
            );
            log.info("Detalle de tomografia con codeReport: {} se mapeo de la siguiente forma: {}", tomography.getCodeReport(), tomographyDetails);

            return tomographyDetails;
        } catch (Exception e) {
            log.error("Error al buscar la categoria de la tomografia {}", tomography.getCodeReport(), e);
        }
            return null;
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        return headers;
    }

}
