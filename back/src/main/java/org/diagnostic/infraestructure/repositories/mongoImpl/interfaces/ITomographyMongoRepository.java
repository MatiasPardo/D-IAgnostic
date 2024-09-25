package org.diagnostic.infraestructure.repositories.mongoImpl.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.diagnostic.domain.entities.Tomography;

import java.util.List;


public interface ITomographyMongoRepository extends MongoRepository<Tomography, String> {
    Tomography findByCodeReport(String codeReport);
    List<Tomography> findByUserId(String userId);
    Page<Tomography> findByUserId(String userId, Pageable pageable);
}
