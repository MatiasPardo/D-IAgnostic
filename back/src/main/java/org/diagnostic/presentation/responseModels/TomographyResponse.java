package org.diagnostic.presentation.responseModels;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.diagnostic.domain.entities.Tomography;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TomographyResponse {

    private List<Tomography> tomographies;

    private boolean successful;

    private String error;

    private PageTomography pagination;

    public TomographyResponse(List<Tomography> tomography, Boolean aTrue, long totalPage, long totalSize) {
        this.tomographies = tomography;
        this.successful = aTrue;
        if(totalPage > 0 || totalSize > 0) this.pagination = new PageTomography(totalPage, totalSize);
    }

    public TomographyResponse(Boolean successful, String error) {
        this.successful = successful;
        this.error = error;
    }
}
