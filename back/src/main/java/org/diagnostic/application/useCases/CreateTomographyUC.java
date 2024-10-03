package org.diagnostic.application.useCases;


import org.diagnostic.domain.entities.Tomography;
import org.diagnostic.infraestructure.repositories.interfaces.ITomographyRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CreateTomographyUC {


    private final ITomographyRepository tomographyRepository;

    private final S3Service s3Service;

    public CreateTomographyUC(ITomographyRepository tomographyRepository, S3Service s3Service) {
        this.tomographyRepository = tomographyRepository;
        this.s3Service = s3Service;
    }

    public Tomography saveAndGenerateUrlTomography(Tomography tomography, byte[] tomographyImage) {
        String codeReport = UUID.randomUUID().toString().substring(0, 8);
        tomography.setCodeReport(codeReport);
        String url = uploadFile(codeReport + tomography.getTomographyDetail().size(), tomographyImage);
        return saveUrl(tomography, url, codeReport);
    }

    public Tomography saveUrl(Tomography tomography, String url, String codeReport) {
        tomography.setTomography(null);
        tomography.addImagesUrl(url);
        tomography.setStatusReport(Tomography.StatusReport.SIN_INFORME);
        tomography.setUpdateDate(LocalDateTime.now());
        return tomographyRepository.save(tomography);
    }

    public Tomography getTomographyStatus(String codeReport) {
        return tomographyRepository.findByCodeReport(codeReport);
    }

    public List<Tomography> getTomography(String userId, String dni, String hc) {
        return tomographyRepository.findByUserId(userId, dni, hc);
    }

    public Page<Tomography> getTomography(String userId, String dni, String hc, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        return tomographyRepository.findByUserId(userId, dni, hc, pageable);

    }

    public String uploadFile(String codeReport, byte[] tomography) {
        return s3Service.uploadFile(codeReport, tomography);
    }
}

