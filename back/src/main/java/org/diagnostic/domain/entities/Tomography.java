package org.diagnostic.domain.entities;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.diagnostic.presentation.responseModels.Response;

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
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Boolean active;
    private List<TomographyDetail> tomographyDetail;

    public enum StatusReport {
        SIN_INFORME,
        GENERACION_EN_PROCESO,
        INFORME_GENERADO
    }


    public void addImagesUrl(TomographyDetail tomographyDetail){
        if (this.tomographyDetail != null)
            this.tomographyDetail.add(tomographyDetail);
        else
            this.tomographyDetail = List.of(tomographyDetail);
    }

    public void addImagesUrl(String url){
        if (this.tomographyDetail != null)
            this.tomographyDetail.add(new TomographyDetail(TomographyCategory.STATELESS, url));

        else
            this.tomographyDetail = List.of(new TomographyDetail(TomographyCategory.STATELESS, url));
    }

}