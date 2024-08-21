package org.tptacs.application.useCases;

import org.springframework.stereotype.Service;
import org.tptacs.domain.entities.Tomography;
import org.tptacs.domain.exceptions.NotFoundTomographyException;
import org.tptacs.infraestructure.repositories.interfaces.ITomographyRepository;

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
