package org.tptacs.presentation.responseModels;

import lombok.Getter;
import org.tptacs.domain.enums.Rol;

@Getter
public class LoginResponse extends Response {
    private String token;
    private String userId;

    public LoginResponse(String token, String userId, String userName, String email, Rol rol) {
        super();
        this.token = token;
        this.userId = userId;
        this.userName = userName;
        this.email = email;
        this.rol = rol;
    }

    private String userName;
    private String email;
    private Rol rol;
}
