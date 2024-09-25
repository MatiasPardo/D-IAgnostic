package org.diagnostic.infraestructure.repositories.mongoImpl.interfaces;

import org.diagnostic.domain.entities.feedback.FeedbackEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IFeedbackMongoRepository extends MongoRepository<FeedbackEntity, String> {

}
