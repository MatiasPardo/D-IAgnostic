package org.tptacs.domain.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.tptacs.presentation.responseModels.Response;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Document(collection = "tomographies")
@Data
public class Tomography extends Response {
    @Id
    private String id;
    private byte[] tomography;
    private String title;
    private String report;
    private String userId;
    private StatusReport statusReport;
    private String codeReport;
    private TomographyCategory category;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Boolean active;
    private List<String> images;

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