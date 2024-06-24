package org.tptacs.infraestructure.repositories.mongoImpl.interfaces;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.tptacs.domain.entities.Item;

public interface IItemsMongoRepository extends MongoRepository<Item, String> {}
