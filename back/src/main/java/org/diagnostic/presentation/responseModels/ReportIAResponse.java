package org.diagnostic.presentation.responseModels;

import lombok.Data;

@Data
public class ReportIAResponse {

    private String model;
    private String response;
    private boolean done;
    private String doneReason;
    private long totalDuration;
    private long loadDuration;
    private int promptEvalCount;
    private long promptEvalDuration;
    private int evalCount;
    private long evalDuration;

}
