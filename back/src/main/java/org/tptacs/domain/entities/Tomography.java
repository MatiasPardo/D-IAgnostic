package org.tptacs.domain.entities;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Tomographies")
@Data
public class Tomography {

    @Id
    private String id;
    private byte[] tomography;
    private String title;
    private String report;
    private String userId;
    private StatusReport statusReport;
    private String codeReport;
    private TomographyCategory category;


    public enum StatusReport {
        SIN_INFORME,
        GENERACION_EN_PROCESO,
        INFORME_GENERADO
    }

    public enum TomographyCategory {
        STONE,
        NORMAL,
        TUMOR,
        CYST,
        STATELESS,
    }
}