package org.tptacs.exceptionHandler;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ErrorResponse {
    private final HttpStatus httpCode;
    private final String message;

    public ErrorResponse(HttpStatus httpCode, String message) {
        this.httpCode = httpCode;
        this.message = message;
    }
}
