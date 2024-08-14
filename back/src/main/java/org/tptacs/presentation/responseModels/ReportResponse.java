package org.tptacs.presentation.responseModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReportResponse extends Response{

    private String codeReport;

    public ReportResponse(String codeReport, String code, String message) {
        super(code,message, LocalDateTime.now());
        this.codeReport = codeReport;
    }
}
