package org.tptacs.presentation.requestModels;

import lombok.Data;

@Data
public class TomographyRequest {
    private byte[] tomography;
    private String title;

}
