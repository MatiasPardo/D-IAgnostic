package org.diagnostic.infraestructure.repositories.mongoImpl.interfaces;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.diagnostic.domain.entities.Item;

public interface IItemsMongoRepository extends MongoRepository<Item, String> {}
