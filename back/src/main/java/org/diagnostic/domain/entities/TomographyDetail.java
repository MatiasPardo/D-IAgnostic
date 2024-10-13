package org.diagnostic.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.diagnostic.presentation.responseModels.TomographyDetailDto;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TomographyDetail {

    private TomographyCategory tomographyCategory = TomographyCategory.STATELESS;

    private String url;

    public TomographyDetailDto toDto() {
        TomographyDetailDto tomographyDetailDto = new TomographyDetailDto();
        tomographyDetailDto.setTomographyCategory(this.getTomographyCategory().getValue());
        tomographyDetailDto.setUrl(this.getUrl());
        return tomographyDetailDto;
    }
}
