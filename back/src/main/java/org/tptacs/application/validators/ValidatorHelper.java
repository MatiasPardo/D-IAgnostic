package org.tptacs.application.validators;

import org.apache.logging.log4j.util.Strings;

import java.util.function.Predicate;

public interface ValidatorHelper {
    default String notNullMessage(String field) {
        return String.format("field '%s' must be not null", field);
    }

    default Predicate<String> notNullOrEmpty() {
        return Strings::isNotBlank;
    }
}
