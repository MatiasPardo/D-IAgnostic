package org.diagnostic.infraestructure.repositories.mongoImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.diagnostic.domain.entities.Tomography;
import org.diagnostic.infraestructure.repositories.interfaces.ITomographyRepository;
import org.diagnostic.infraestructure.repositories.mongoImpl.interfaces.ITomographyMongoRepository;

import java.util.List;

@Repository
@Primary
public class TomographyMongoRepository implements ITomographyRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private ITomographyMongoRepository ITomographyMongoRepository;

    @Override
    public Tomography save(Tomography tomography) {
        return ITomographyMongoRepository.save(tomography);
    }

    @Override
    public Tomography findByCodeReport(String codeReport) {
        return ITomographyMongoRepository.findByCodeReport(codeReport);
    }

    @Override
    public List<Tomography> findByUserId(String userId, String dni, String hc, String title) {
        return this.findByUserIdDniAndHistoriaClinica(userId, dni, hc, title);
    }

    @Override
    public Page<Tomography> findByUserId(String userId, String dni, String hc, String title, Pageable pageable) {
        return this.findByUserIdDniAndHistoriaClinica(userId, dni, hc, title, pageable);
    }

    @Override
    public void delete(Tomography tomography) {
        ITomographyMongoRepository.delete(tomography);
    }

    public List<Tomography> findByUserIdDniAndHistoriaClinica(String userId, String dni, String historiaClinica, String title) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));

        if (dni != null && !dni.isEmpty()) {
            query.addCriteria(Criteria.where("patient.document").is(dni));
        }

        if (historiaClinica != null && !historiaClinica.isEmpty()) {
            query.addCriteria(Criteria.where("patient.clinicHistory").is(historiaClinica));
        }

        if (title != null && !title.isEmpty()) {
            query.addCriteria(Criteria.where("title").regex(".*" + title + ".*", "i")); // "i" para ignorar mayúsculas y minúsculas
        }

        // Aplicar el ordenamiento por la fecha de creación
        query = applySorting(query);

        return mongoTemplate.find(query, Tomography.class);
    }



    public Page<Tomography> findByUserIdDniAndHistoriaClinica(String userId, String dni, String historiaClinica, String title, Pageable pageable) {
        Query query = new Query();

        // Filtro por userId (obligatorio)
        query.addCriteria(Criteria.where("userId").is(userId));

        // Filtro por patient.document (dni) si no está vacío ni null
        if (dni != null && !dni.isEmpty()) {
            query.addCriteria(Criteria.where("patient.document").is(dni));
        }

        if (historiaClinica != null && !historiaClinica.isEmpty()) {
            query.addCriteria(Criteria.where("patient.clinicHistory").is(historiaClinica));
        }

        if (title != null && !title.isEmpty()) {
            query.addCriteria(Criteria.where("title").regex(".*" + title + ".*", "i")); // "i" para ignorar mayúsculas y minúsculas
        }

        // Aplicar el ordenamiento por la fecha de creación
        query = applySorting(query);

        // Establecer la paginación
        query.with(pageable);

        // Ejecutar la consulta paginada
        List<Tomography> tomographies = mongoTemplate.find(query, Tomography.class);

        // Obtener el total de resultados (sin paginación) para poder armar la página correctamente
        long count = mongoTemplate.count(query.skip(-1).limit(-1), Tomography.class);

        // Crear el objeto Page para devolver resultados paginados
        return new PageImpl<>(tomographies, pageable, count);
    }


    private Query applySorting(Query query) {
        // Ordenar por la fecha de creación en orden descendente (la más reciente primero)
        query.with(Sort.by(Sort.Direction.DESC, "createDate"));
        return query;
    }


}
