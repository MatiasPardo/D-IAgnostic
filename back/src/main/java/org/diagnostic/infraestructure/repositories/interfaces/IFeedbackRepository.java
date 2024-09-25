package org.diagnostic.infraestructure.repositories.interfaces;

import org.diagnostic.domain.entities.feedback.FeedbackEntity;

public interface IFeedbackRepository {

    void save(FeedbackEntity feedback);
}
