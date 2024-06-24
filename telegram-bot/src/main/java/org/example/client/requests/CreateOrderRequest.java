package org.example.client.requests;

import lombok.Getter;

@Getter
public class CreateOrderRequest {
    private String name;

    public CreateOrderRequest(String name) {
        this.name = name;
    }
}
