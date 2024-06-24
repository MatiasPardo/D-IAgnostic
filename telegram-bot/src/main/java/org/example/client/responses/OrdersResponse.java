package org.example.client.responses;

import lombok.Getter;
import java.util.List;

@Getter
public class OrdersResponse {
    private List<OrderResponse> orderDtos;

    @Getter
    public static class OrderResponse {
        private String name;
        private String orderId;
        private String status;
    }
}
