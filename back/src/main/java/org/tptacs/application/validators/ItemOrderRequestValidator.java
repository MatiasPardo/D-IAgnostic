package org.tptacs.application.validators;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.tptacs.presentation.requestModels.ItemOrderRequest;

import static br.com.fluentvalidator.predicate.ComparablePredicate.greaterThanOrEqual;
import static br.com.fluentvalidator.predicate.ObjectPredicate.nullValue;
import static java.util.function.Predicate.not;

@Component
public class ItemOrderRequestValidator extends Validator<ItemOrderRequest> implements ValidatorHelper {
    @Override
    public void rules() {
        ruleFor(ItemOrderRequest::getId)
            .must(notNullOrEmpty())
                .withMessage(notNullMessage("id"))
                .withCode(HttpStatus.UNPROCESSABLE_ENTITY.name());

        ruleFor(ItemOrderRequest::getQuantity)
                .must(not(nullValue()))
                    .withMessage(notNullMessage("quantity"))
                    .withCode(HttpStatus.UNPROCESSABLE_ENTITY.name())
                .must(greaterThanOrEqual(1L))
                    .withMessage("quantity must be greater than or equal to 1")
                    .withCode(HttpStatus.UNPROCESSABLE_ENTITY.name());
    }

    @Override
    public Class<?> getClazz() {
        return ItemOrderRequest.class;
    }
}
