package org.tptacs.exceptionHandler;

import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.tptacs.domain.exceptions.*;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        System.out.println("error no manejado: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()));
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        System.out.println("error: " + ex.getMessage());
        var error = new ErrorResponse(getHttpStatus(ex.getCodes()), ex.getMessage());
        return ResponseEntity.status(error.getHttpCode()).body(error);
    }
    
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        System.out.println("error: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(HttpStatus.NOT_FOUND, ex.getMessage()));
    }
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlConflictException(ResourceNotFoundException ex) {
        return handleConflict(ex);
    }
    
    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<ErrorResponse> handlRegistrationException(RegistrationException ex) {
        return handleConflict(ex);
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        System.out.println("error: " + ex.getMessage());
        var error = new ErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(error.getHttpCode()).body(error);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationException(AuthorizationException ex) {
        System.out.println("error: " + ex.getMessage());
        var error = new ErrorResponse(HttpStatus.FORBIDDEN, ex.getMessage());
        return ResponseEntity.status(error.getHttpCode()).body(error);
    }

    private HttpStatus getHttpStatus(Set<String> codes) {
        if (codes.contains(HttpStatus.UNPROCESSABLE_ENTITY.name())) return HttpStatus.UNPROCESSABLE_ENTITY;
        if (codes.contains(HttpStatus.CONFLICT.name())) return HttpStatus.CONFLICT;
        return HttpStatus.BAD_REQUEST;
    }
    
	private ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex) {
		System.out.println("error: " + ex.getMessage());
        var error = new ErrorResponse(HttpStatus.CONFLICT, ex.getMessage());
        return ResponseEntity.status(error.getHttpCode()).body(error);
	}

}
