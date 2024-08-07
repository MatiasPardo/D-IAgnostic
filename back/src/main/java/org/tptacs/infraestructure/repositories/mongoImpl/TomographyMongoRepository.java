package org.tptacs.infraestructure.repositories.mongoImpl;


import io.github.classgraph.AnnotationInfoList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.tptacs.domain.entities.Tomography;
import org.tptacs.infraestructure.repositories.interfaces.ITomographyRepository;
import org.tptacs.infraestructure.repositories.mongoImpl.interfaces.ITomographyMongoRepository;

import java.util.List;

@Repository
@Primary
public class TomographyMongoRepository implements ITomographyRepository {

    @Autowired
    private ITomographyMongoRepository ITomographyMongoRepository;

    @Override
    public void save(Tomography tomography) {
        ITomographyMongoRepository.save(tomography);
    }

    @Override
    public Tomography findByCodeReport(String codeReport) {
        return ITomographyMongoRepository.findByCodeReport(codeReport);
    }

    @Override
    public List<Tomography> findByUserId(String codeReport) {
        return ITomographyMongoRepository.findByUserId(codeReport);
    }
}