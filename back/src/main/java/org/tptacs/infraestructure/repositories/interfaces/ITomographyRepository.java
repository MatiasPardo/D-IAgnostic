package org.tptacs.infraestructure.repositories.interfaces;

import io.github.classgraph.AnnotationInfoList;
import org.tptacs.domain.entities.Tomography;

public interface ITomographyRepository {

    void save(Tomography tomography);

    Tomography findByCodeReport(String codeReport);
}
