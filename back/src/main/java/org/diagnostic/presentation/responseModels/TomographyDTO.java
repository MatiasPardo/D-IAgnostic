package org.diagnostic.presentation.responseModels;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.diagnostic.domain.entities.Patient;
import org.diagnostic.domain.entities.Tomography;
import org.diagnostic.domain.entities.TomographyDetail;

import java.time.LocalDateTime;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class TomographyDTO extends Response{

    private String title;
    private String report;
    private String userId;
    private Tomography.StatusReport statusReport;
    private String codeReport;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime updateDate;
    private Boolean active;
    private List<TomographyDetailDto> images;
    private Patient patient;


}
