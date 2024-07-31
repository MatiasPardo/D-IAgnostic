package org.tptacs.domain.entities;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
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
    private Status status;
    private String codeReport;


    public enum Status {
        SIN_INFORME,
        GENERACION_EN_PROCESO,
        INFORME_GENERADO
    }
}