package org.diagnostic.application.useCases;

import lombok.extern.slf4j.Slf4j;
import org.diagnostic.domain.entities.TomographyCategory;
import org.diagnostic.domain.entities.TomographyDetail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ImageCategorizationSummary {

    public String summary(List<TomographyDetail> tomographyDetails) {
        if (tomographyDetails == null || tomographyDetails.isEmpty()) {
            throw new RuntimeException("No se puede obtener un resumen porque no hay ninguna detalle de tomografía");
        }

        List<TomographyDetail> stone = tomographyDetails.stream()
                .filter(detail -> detail.getTomographyCategory().equals(TomographyCategory.Stone))
                .toList();

        List<TomographyDetail> normal = tomographyDetails.stream()
                .filter(detail -> detail.getTomographyCategory().equals(TomographyCategory.Normal))
                .toList();

        List<TomographyDetail> tumor = tomographyDetails.stream()
                .filter(detail -> detail.getTomographyCategory().equals(TomographyCategory.Tumor))
                .toList();

        List<TomographyDetail> cyst = tomographyDetails.stream()
                .filter(detail -> detail.getTomographyCategory().equals(TomographyCategory.Cyst))
                .toList();

        StringBuilder summaryBuilder = new StringBuilder("El paciente es analizado y en su tomografía se resume en que: ");

        if (!stone.isEmpty() && tumor.isEmpty() && cyst.isEmpty()) {
            summaryBuilder.append("al visualizar las imágenes se encontró al menos ")
                    .append(stone.size())
                    .append(" imagen(es) con presencia de piedras.");
        } else if (!tumor.isEmpty() && stone.isEmpty() && cyst.isEmpty()) {
            summaryBuilder.append("al visualizar las imágenes se encontró al menos ")
                    .append(tumor.size())
                    .append(" imagen(es) con presencia de tumores.");
        } else if (!cyst.isEmpty() && stone.isEmpty() && tumor.isEmpty()) {
            summaryBuilder.append("al visualizar las imágenes se encontró al menos ")
                    .append(cyst.size())
                    .append(" imagen(es) con presencia de quistes.");
        } else if (stone.isEmpty() && tumor.isEmpty() && cyst.isEmpty()) {
            summaryBuilder.append("al visualizar las imágenes no se encontró ningún problema.");
        } else {
            if (!stone.isEmpty()) {
                summaryBuilder.append("al visualizar las imágenes se encontró al menos ")
                        .append(stone.size())
                        .append(" imagen(es) con presencia de piedras. ");
            }
            if (!tumor.isEmpty()) {
                summaryBuilder.append("al visualizar las imágenes se encontró al menos ")
                        .append(tumor.size())
                        .append(" imagen(es) con presencia de tumores. ");
            }
            if (!cyst.isEmpty()) {
                summaryBuilder.append("al visualizar las imágenes se encontró al menos ")
                        .append(cyst.size())
                        .append(" imagen(es) con presencia de quistes.");
            }
        }

        return summaryBuilder.toString().trim();
    }

}
