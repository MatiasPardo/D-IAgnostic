package org.diagnostic.infraestructure.repositories.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.diagnostic.domain.entities.Tomography;

import java.util.List;

public interface ITomographyRepository {

    Tomography save(Tomography tomography);
    Tomography findByCodeReport(String codeReport);
    List<Tomography> findByUserId(String codeReport, String dni, String hc);
    Page<Tomography> findByUserId(String codeReport, String dni, String hc, Pageable pageable);
    void delete(Tomography tomography);

}
