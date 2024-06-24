package org.tptacs.presentation.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public class OrderDto {
    @JsonProperty("orderId")
    private String orderId;
    @JsonProperty("userId")
    private String userId;
    @JsonProperty("status")
    private String status;
    @JsonProperty("hasItems")
    private boolean hasItems;
    @JsonProperty("name")
    private String name;

    @JsonCreator
    public OrderDto(String orderId, String userId, String status, boolean hasItems, String name) {
        this.orderId = orderId;
        this.userId = userId;
        this.status = status;
        this.hasItems = hasItems;
        this.name = name;
    }
}