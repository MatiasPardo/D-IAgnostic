package org.diagnostic.application.useCases;

import lombok.extern.slf4j.Slf4j;
import org.diagnostic.application.services.IARestClient;
import org.diagnostic.domain.entities.Tomography;
import org.diagnostic.domain.entities.TomographyCategory;
import org.diagnostic.domain.entities.TomographyDetail;
import org.diagnostic.infraestructure.repositories.interfaces.ITomographyRepository;
import org.diagnostic.presentation.responseModels.ReportIAResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@Slf4j
public class CategorizeAndGenerateReportUC {

    @Value("${report.other}")
    private String reportOther;

    @Autowired
    private final RestTemplate restTemplate;

    private final IARestClient iARestClient;

    private final ITomographyRepository tomographyRepository;

    public CategorizeAndGenerateReportUC(RestTemplate restTemplate, IARestClient IARestClient, ITomographyRepository tomographyRepository) {
        this.restTemplate = restTemplate;
        this.iARestClient = IARestClient;
        this.tomographyRepository = tomographyRepository;
    }

    @Async
    public void categorizeAndGenerateReport(Tomography tomography){
        tomography.setStatusReport(Tomography.StatusReport.GENERACION_EN_PROCESO);
        List<TomographyDetail> tomographyCategory = iARestClient.categorizeTomography(tomography);

        if(tomographyCategory != null) {
            log.info("Tomografia {} categorizada correctamente: {}",tomography.getCodeReport(), tomographyCategory);
            tomography.setTomographyDetail(tomographyCategory);
            tomographyRepository.save(tomography);
        }

        if(tomographyCategory != null && tomographyCategory.stream().anyMatch(t-> t.getTomographyCategory().equals(TomographyCategory.Other))){
            tomography.setStatusReport(Tomography.StatusReport.NO_CORRESPONDE_INFORME);
            tomography.setReport(reportOther);
            tomographyRepository.save(tomography);
        }else{
            ReportIAResponse report = iARestClient.findReport(tomography);
            if (report != null) {
                tomography.setStatusReport(Tomography.StatusReport.INFORME_GENERADO);
                tomography.setReport(report.getResponse());
                tomographyRepository.save(tomography);
            }
        }

    }

}
