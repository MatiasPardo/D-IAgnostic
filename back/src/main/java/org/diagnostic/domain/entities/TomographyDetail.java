package org.diagnostic.domain.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TomographyDetail {

    private TomographyCategory tomographyCategory = TomographyCategory.STATELESS;

    private String url;


}
