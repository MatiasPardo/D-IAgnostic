package org.tptacs.application.validators;

import br.com.fluentvalidator.AbstractValidator;

public abstract class Validator<T> extends AbstractValidator<T> {
    public abstract Class<?> getClazz();
}
