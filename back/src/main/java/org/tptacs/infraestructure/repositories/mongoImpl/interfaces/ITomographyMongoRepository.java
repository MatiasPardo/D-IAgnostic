package org.tptacs.infraestructure.repositories.mongoImpl.interfaces;

import io.github.classgraph.AnnotationInfoList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.tptacs.domain.entities.Tomography;

public interface ITomographyMongoRepository extends MongoRepository<Tomography, String> {
    Tomography findByCodeReport(String codeReport);
}
