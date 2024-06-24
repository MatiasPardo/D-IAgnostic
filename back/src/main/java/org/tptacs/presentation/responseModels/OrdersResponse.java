package org.tptacs.presentation.responseModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import org.tptacs.presentation.dto.OrderDto;

import java.util.List;

public class OrdersResponse extends Response{
    @Schema(description = "DTOs of orders")
    @JsonProperty("orderDtos")
    private List<OrderDto> orderDtos;

    @JsonCreator
    public OrdersResponse(List<OrderDto> orderDtos) {
        super();
        this.orderDtos = orderDtos;
    }

}
