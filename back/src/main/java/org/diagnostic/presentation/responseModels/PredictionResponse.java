package org.diagnostic.presentation.responseModels;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.diagnostic.domain.entities.Tomography;
import org.diagnostic.domain.entities.TomographyCategory;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PredictionResponse {

    private String message;
    @JsonProperty("images_by_class")
    private Map<String, List<String>> images_by_class;
    private String status;

    @Override
    public String toString() {
        return "PredictionResponse{" +
                "message='" + message + '\'' +
                ", images_by_class='" + images_by_class + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
