package org.example.client.requests;

import lombok.Getter;

@Getter
public class RegisterRequest {
    private String userName;
    private String email;
    private String password;

    public RegisterRequest(String userName, String mail, String password) {
        this.userName = userName;
        this.email = mail;
        this.password = password;
    }
}
