package org.tptacs.infraestructure.repositories.interfaces;

import io.github.classgraph.AnnotationInfoList;
import org.tptacs.domain.entities.Tomography;

import java.util.List;

public interface ITomographyRepository {

    void save(Tomography tomography);

    Tomography findByCodeReport(String codeReport);
    List<Tomography> findByUserId(String codeReport);
}
