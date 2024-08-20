package org.example.entities;

import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class Tomography {
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

    @Getter
    public enum StatusReport {
        SIN_INFORME("Sin informe"),
        GENERACION_EN_PROCESO("Solicitud de informe en proceso"),
        INFORME_GENERADO("Informe finalizado");

        String desc;

        StatusReport(String s) {
            this.desc = s;
        }

    }

    @Getter
    public enum TomographyCategory {
        STONE("Con piedra"),
        NORMAL("Sin piedra"),
        TUMOR("Con tumor"),
        CYST("Con piedra"),
        STATELESS("Categorizacion en proceso"),
        ;

        String desc;

        TomographyCategory(String s) {
            this.desc = s;
        }
    }
}
