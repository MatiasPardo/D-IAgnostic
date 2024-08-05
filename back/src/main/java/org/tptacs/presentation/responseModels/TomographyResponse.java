package org.tptacs.presentation.responseModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.tptacs.domain.entities.Tomography;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TomographyResponse {

    private List<Tomography> tomographies;

    private boolean successful;

    private String error;

    public TomographyResponse(List<Tomography> tomography, Boolean aTrue) {
        this.tomographies = tomography;
        this.successful = aTrue;
    }

    public TomographyResponse(Boolean successful, String error) {
        this.successful = successful;
        this.error = error;
    }
}
