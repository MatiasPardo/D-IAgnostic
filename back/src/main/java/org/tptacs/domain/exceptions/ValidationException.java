package org.tptacs.domain.exceptions;

import br.com.fluentvalidator.context.Error;
import br.com.fluentvalidator.context.ValidationResult;
import lombok.Getter;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
public class ValidationException extends RuntimeException {
    private String message;
    private Set<String> codes;

    public ValidationException(ValidationResult validationResult) {
        this.message = validationResult.getErrors().stream().map(Error::getMessage).collect(Collectors.joining(", "));
        this.codes = new HashSet<>(validationResult.getErrors().stream().map(Error::getCode).toList());
    }
}
