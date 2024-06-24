package org.tptacs.infraestructure.repositories.mongoImpl.interfaces;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.tptacs.domain.entities.Order;

public interface IOrderMongoRepository extends MongoRepository<Order, String> {}
