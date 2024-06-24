package org.tptacs.presentation.responseModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

import org.tptacs.domain.entities.Order;

public class OrderResponse extends Response{

	
	@Schema(description = "idientifier of order")
	@JsonProperty("orderId")
	private String id;
	
	@Schema(description = "name of order")
	@JsonProperty("name")
	private String name;
	
	@Schema(description = "status of order")
    @JsonProperty("status")
    private String status;
    
	@Schema(description = "if has items")
    @JsonProperty("hasItems")
    private boolean hasItems;
    
	@Schema(description = "shared order user")
    @JsonProperty("userId")
    private boolean userId;

	@JsonCreator
    public OrderResponse(Order order, String code, String message) {
		super(code, message, LocalDateTime.now());
		this.id = order.getId();
		this.name = order.getName();
		this.hasItems = !order.getItems().isEmpty();
		this.status = order.getStatus().name();
    }

	public OrderResponse(Order order) {
		super();
		this.id = order.getId();
		this.name = order.getName();
		this.hasItems = !order.getItems().isEmpty();
		this.status = order.getStatus().name();	}
}
