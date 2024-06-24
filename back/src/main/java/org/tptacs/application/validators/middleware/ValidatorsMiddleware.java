package org.tptacs.application.validators.middleware;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;
import org.tptacs.domain.exceptions.ValidationException;
import org.tptacs.application.validators.Validator;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

@RestControllerAdvice
public class ValidatorsMiddleware implements RequestBodyAdvice {

    private final List<Validator> validators;

    public ValidatorsMiddleware(List<Validator> validators) {
        this.validators = validators;
    }

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) throws IOException {
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        Validator validator = this.validators.stream().filter(v -> v.getClazz().equals(body.getClass()))
                .findFirst().orElse(null);

        if (validator == null) return body;

        var validationResult = validator.validate(body);
        if (!validationResult.isValid()) {
            throw new ValidationException(validationResult);
        }
        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }
}
