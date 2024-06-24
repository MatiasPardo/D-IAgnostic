package org.example.exceptions;

public class RestException extends RuntimeException {
    public RestException(String message) {
        super(message);
    }
}
