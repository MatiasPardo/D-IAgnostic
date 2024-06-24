package org.tptacs.infraestructure.repositories.mongoImpl.interfaces;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.tptacs.domain.entities.User;

public interface IUserMongoRepository extends MongoRepository<User, String> {}
