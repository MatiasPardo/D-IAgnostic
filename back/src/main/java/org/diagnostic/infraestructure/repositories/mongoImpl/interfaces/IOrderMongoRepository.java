package org.diagnostic.infraestructure.repositories.mongoImpl.interfaces;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.diagnostic.domain.entities.Order;

public interface IOrderMongoRepository extends MongoRepository<Order, String> {}
