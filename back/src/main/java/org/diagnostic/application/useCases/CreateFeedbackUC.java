package org.diagnostic.application.useCases;

import org.diagnostic.infraestructure.repositories.interfaces.IFeedbackRepository;
import org.diagnostic.presentation.dto.feedback.Feedback;
import org.springframework.stereotype.Service;

@Service
public class CreateFeedbackUC {

    private final IFeedbackRepository iFeedbackRepository;

    public CreateFeedbackUC(IFeedbackRepository iFeedbackRepository) {
        this.iFeedbackRepository = iFeedbackRepository;
    }

    public void saveFeedback(Feedback feedback){
        iFeedbackRepository.save(feedback.toEntity());
    }
}
