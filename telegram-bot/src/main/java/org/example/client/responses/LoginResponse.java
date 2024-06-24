package org.example.client.responses;

import lombok.Getter;

@Getter
public class LoginResponse {
    private String token;
    private String userName;
    private String userId;
}
