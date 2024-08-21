package org.diagnostic.application.useCases;

import org.springframework.stereotype.Service;
import org.diagnostic.domain.entities.Tomography;
import org.diagnostic.domain.exceptions.NotFoundTomographyException;
import org.diagnostic.infraestructure.repositories.interfaces.ITomographyRepository;

@Service
public class DeleteTomographyUC {

    private final ITomographyRepository tomographyRepository;

    public DeleteTomographyUC(ITomographyRepository tomographyRepository) {
        this.tomographyRepository = tomographyRepository;
    }

    public void deleteTomography(String codeReport, String userId) throws NotFoundTomographyException {
        Tomography tomography = tomographyRepository.findByCodeReport(codeReport);
        if(tomography == null || !tomography.getActive() || !tomography.getUserId().equals(userId)) throw new NotFoundTomographyException();
        tomographyRepository.delete(tomography);
    }
}
