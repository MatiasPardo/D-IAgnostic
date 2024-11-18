package org.diagnostic.presentation.responseModels;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RecoveryResponse extends Response{

    private String token;

    public RecoveryResponse(String code, String message, LocalDateTime date, String token) {
        super(code, message, date);
        this.token = token;
    }
}
