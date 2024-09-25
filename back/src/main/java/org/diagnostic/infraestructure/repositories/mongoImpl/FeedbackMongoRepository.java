package org.diagnostic.infraestructure.repositories.mongoImpl;

import org.diagnostic.domain.entities.feedback.FeedbackEntity;
import org.diagnostic.infraestructure.repositories.interfaces.IFeedbackRepository;
import org.diagnostic.infraestructure.repositories.mongoImpl.interfaces.IFeedbackMongoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

@Repository
@Primary
public class FeedbackMongoRepository implements IFeedbackRepository {

    @Autowired
    private IFeedbackMongoRepository iFeedbackMongoRepository;
    @Override
    public void save(FeedbackEntity feedback) {
        iFeedbackMongoRepository.save(feedback);
    }
}
