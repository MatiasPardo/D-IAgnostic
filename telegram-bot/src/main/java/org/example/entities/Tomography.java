package org.example.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Tomography {

    private byte[] image;
    private String title;
    private String report;
    private String userId;
    private StatusReport statusReport;
    private String codeReport;
    //@JsonFormat(pattern = "dd/mm/yyyy HH:mm:ss")
    private String createdDate;
    //@JsonFormat(pattern = "dd/mm/yyyy HH:mm:ss")
    private String updateDate;
    private Boolean active;
    private List<TomographyDetail> tomographyDetail;
    private Patient patient;

    public Tomography(String title, byte[] tac) {
        this.title = title;
        this.image = tac;
    }

    @Getter
    public enum StatusReport {
        SIN_INFORME("Sin informe"),
        GENERACION_EN_PROCESO("Solicitud en proceso"),
        INFORME_GENERADO("Finalizado"),
        NO_CORRESPONDE_INFORME("No corresponde imagen por la clasificacion encontrada en la/s imagen/es");
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
