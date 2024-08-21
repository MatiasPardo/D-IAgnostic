package org.diagnostic.infraestructure.repositories.mongoImpl.interfaces;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.diagnostic.domain.entities.User;

public interface IUserMongoRepository extends MongoRepository<User, String> {}
