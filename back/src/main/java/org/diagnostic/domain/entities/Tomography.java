package org.diagnostic.domain.entities;

import lombok.Data;
import org.diagnostic.presentation.responseModels.TomographyDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Document(collection = "tomographies")
@Data
public class Tomography{

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
    private Patient patient;

    public TomographyDTO dto() {
        TomographyDTO tomographyDTO = new TomographyDTO();
        if(this.tomographyDetail != null) tomographyDTO.setImages(tomographyDetail.stream().map(TomographyDetail::getUrl).toList());
        tomographyDTO.setCodeReport(this.codeReport);
        tomographyDTO.setTitle(this.title);
        tomographyDTO.setStatusReport(this.statusReport);
        tomographyDTO.setCreateDate(this.createDate);
        tomographyDTO.setUpdateDate(this.updateDate);
        tomographyDTO.setReport(this.report);
        tomographyDTO.setUserId(this.userId);
        tomographyDTO.setPatient(this.patient);
        return tomographyDTO;
    }

    public enum StatusReport {
        SIN_INFORME,
        GENERACION_EN_PROCESO,
        INFORME_GENERADO,
        NO_CORRESPONDE_INFORME;
    }


    public List<TomographyDetail> getTomographyDetail(){
        if (this.tomographyDetail != null)
            return this.tomographyDetail;
        else
            return new LinkedList<TomographyDetail>();
    }

    public void addImagesUrl(String url){
        if (this.tomographyDetail != null)
            this.tomographyDetail.add(new TomographyDetail(TomographyCategory.STATELESS, url));

        else
            this.tomographyDetail = List.of(new TomographyDetail(TomographyCategory.STATELESS, url));
    }

}