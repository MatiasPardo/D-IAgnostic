package org.diagnostic.infraestructure.repositories.mongoImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.diagnostic.domain.entities.Tomography;
import org.diagnostic.infraestructure.repositories.interfaces.ITomographyRepository;
import org.diagnostic.infraestructure.repositories.mongoImpl.interfaces.ITomographyMongoRepository;

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
    public List<Tomography> findByUserId(String userId) {
        return ITomographyMongoRepository.findByUserId(userId);
    }

    @Override
    public Page<Tomography> findByUserId(String userId, Pageable pageable) {
        return ITomographyMongoRepository.findByUserId(userId, pageable);
    }

    @Override
    public void delete(Tomography tomography) {
        ITomographyMongoRepository.delete(tomography);
    }

}
