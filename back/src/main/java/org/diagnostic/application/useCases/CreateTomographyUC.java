package org.diagnostic.application.useCases;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.diagnostic.domain.entities.Tomography;
import org.diagnostic.infraestructure.repositories.interfaces.ITomographyRepository;

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

    public String saveTomography(Tomography tomography) {
        String codeReport = UUID.randomUUID().toString().substring(0, 8);
        String url = s3Service.uploadFile(codeReport, tomography.getTomography());
        tomography.setImages(List.of(url));
        tomography.setCodeReport(codeReport);
        tomography.setStatusReport(Tomography.StatusReport.SIN_INFORME);
        tomography.setCategory(Tomography.TomographyCategory.STATELESS);
        tomography.setUpdateDate(LocalDateTime.now());
        tomography.setActive(Boolean.TRUE);
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

