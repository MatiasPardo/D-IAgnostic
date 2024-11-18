package org.diagnostic.presentation.requestModels;

import lombok.Data;

@Data
public class ChangePassRequest {

    private String code;

    private String token;

    private String email;

    private String newPassword;
}
