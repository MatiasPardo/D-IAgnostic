package org.diagnostic.presentation.requestModels;

import lombok.Getter;

@Getter
public class CreateUserRequest {
    private String userName;
    private String password;
    private String email;
}
