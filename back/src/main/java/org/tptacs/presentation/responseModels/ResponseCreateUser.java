package org.tptacs.presentation.responseModels;

import lombok.Getter;

@Getter
public class ResponseCreateUser extends Response {
    private String userId;

    public ResponseCreateUser(String userId) {
        super();
        this.userId = userId;
    }
}
