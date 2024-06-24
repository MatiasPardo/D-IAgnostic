package org.tptacs.application.validators;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.tptacs.presentation.requestModels.ItemOrderRequest;

import br.com.fluentvalidator.context.ValidationResult;

public class ItemOrderRequestValidatorTest {

    private ItemOrderRequestValidator validator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new ItemOrderRequestValidator();
    }

    @Test
    public void validateValidItemOrderRequest() {
        ItemOrderRequest itemOrderRequest = new ItemOrderRequest("item-id", 2L);

        ValidationResult validationResult = validator.validate(itemOrderRequest);

        assertThat(validationResult.isValid()).isTrue();
    }

    @Test
    public void validateItemOrderRequestWithEmptyId() {
        ItemOrderRequest itemOrderRequest = new ItemOrderRequest("", 2L);

        ValidationResult validationResult = validator.validate(itemOrderRequest);

        assertThat(validationResult.isValid()).isFalse();
        assertThat(validationResult.getErrors()).hasSize(1);
        assertThat(validationResult.getErrors().stream().findFirst().get().getCode()).isEqualTo("UNPROCESSABLE_ENTITY");
    }

    @Test
    public void validateItemOrderRequestWithZeroQuantity() {
        ItemOrderRequest itemOrderRequest = new ItemOrderRequest("item-id", 0L);

        ValidationResult validationResult = validator.validate(itemOrderRequest);

        assertThat(validationResult.isValid()).isFalse();
        assertThat(validationResult.getErrors()).hasSize(1);
        assertThat(validationResult.getErrors().stream().findFirst().get().getCode()).isEqualTo("UNPROCESSABLE_ENTITY");
    }

}
