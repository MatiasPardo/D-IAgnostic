package org.tptacs.presentation.responseModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictionResponse {

    private String message;
    @JsonProperty("predicted_class")
    private String predictedClass;
    private String status;

    @Override
    public String toString() {
        return "PredictionResponse{" +
                "message='" + message + '\'' +
                ", predictedClass='" + predictedClass + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
