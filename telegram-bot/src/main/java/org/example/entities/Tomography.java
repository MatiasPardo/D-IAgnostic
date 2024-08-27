package org.example.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Tomography {
    private String title;
    private String report;
    private String userId;
    private byte[] image;
    private StatusReport statusReport;
    private String codeReport;
    private TomographyCategory category;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateDate;
    private Boolean active;
    private List<String> images;

    public Tomography(String title, byte[] tac) {
        this.title = title;
        this.image = tac;
    }

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
