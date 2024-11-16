package org.diagnostic.infraestructure.repositories.interfaces;

import org.diagnostic.domain.entities.feedback.FeedbackEntity;
import org.diagnostic.presentation.dto.feedback.Feedback;

public interface IFeedbackRepository {

    void save(FeedbackEntity feedback);
    FeedbackEntity find(String codeReport);
}
