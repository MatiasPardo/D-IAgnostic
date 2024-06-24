package org.tptacs.domain.exceptions;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException() {
        super("invalid token");
    }
}
