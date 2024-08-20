package org.tptacs.infraestructure.repositories.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.tptacs.domain.entities.Tomography;

import java.util.List;

public interface ITomographyRepository {

    void save(Tomography tomography);
    Tomography findByCodeReport(String codeReport);
    List<Tomography> findByUserId(String codeReport);
    Page<Tomography> findByUserId(String codeReport, Pageable pageable);
    void delete(Tomography tomography);

}
