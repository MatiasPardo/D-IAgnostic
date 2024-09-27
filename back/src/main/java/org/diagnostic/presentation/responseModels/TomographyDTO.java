package org.diagnostic.presentation.responseModels;

import lombok.Data;
import org.diagnostic.domain.entities.Patient;
import org.diagnostic.domain.entities.Tomography;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class TomographyDTO extends Response{

    private String title;
    private String report;
    private String userId;
    private Tomography.StatusReport statusReport;
    private String codeReport;
    private LocalDateTime createDate;
    private LocalDateTime updateDate;
    private Boolean active;
    private List<String> images;
    private Patient patient;

}
