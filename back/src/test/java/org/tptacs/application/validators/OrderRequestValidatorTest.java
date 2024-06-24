package org.tptacs.application.validators;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.tptacs.presentation.requestModels.ItemOrderRequest;
import org.tptacs.presentation.requestModels.OrderRequest;

import br.com.fluentvalidator.context.ValidationResult;

public class OrderRequestValidatorTest {

    private OrderRequestValidator validator;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new OrderRequestValidator();
    }

    @Test
    public void validateValidOrderRequest() {
        OrderRequest orderRequest = new OrderRequest(List.of(new ItemOrderRequest("item-id", 2L)));

        ValidationResult validationResult = validator.validate(orderRequest);

        assertThat(validationResult.isValid()).isTrue();
    }


    @Test
    public void validateOKOrderRequestWithEmptyItems() {
        OrderRequest orderRequest = new OrderRequest(null);

        ValidationResult validationResult = validator.validate(orderRequest);

        assertThat(validationResult.isValid()).isTrue();

    }

}
