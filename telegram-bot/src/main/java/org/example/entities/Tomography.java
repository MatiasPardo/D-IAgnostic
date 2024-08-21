package org.example.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDate;
    private Boolean active;
    private List<String> images;

    @Getter
    public enum StatusReport {
        SIN_INFORME("Sin informe"),
        GENERACION_EN_PROCESO("Solicitud en proceso"),
        INFORME_GENERADO("Finalizado");

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
