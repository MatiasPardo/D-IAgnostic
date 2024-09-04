package org.diagnostic.domain.entities;

import lombok.Data;

@Data
public class TomographyImage {

    private String url;
    private TomographyCategory category;

}
