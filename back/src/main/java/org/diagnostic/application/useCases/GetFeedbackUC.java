package org.diagnostic.application.useCases;

import org.diagnostic.infraestructure.repositories.interfaces.IFeedbackRepository;
import org.diagnostic.presentation.dto.feedback.Feedback;
import org.springframework.stereotype.Service;

@Service
public class GetFeedbackUC {

    private final IFeedbackRepository iFeedbackRepository;

    public GetFeedbackUC(IFeedbackRepository iFeedbackRepository) {
        this.iFeedbackRepository = iFeedbackRepository;
    }

    public Feedback get(String codeReport){
        return this.iFeedbackRepository.find(codeReport).toDomain();

    }

}
