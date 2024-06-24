package org.tptacs.presentation.responseModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;

public class AnalyticsResponse extends Response {
	
	
	@Schema(description = "Response internal code")
	@JsonProperty("usersQuantity")
	private Long usersQuantity;
	@JsonProperty("ordersQuantity")
	private Long ordersQuantity;

	@JsonCreator
	public AnalyticsResponse(Long usersQuantity, Long ordersQuantity) {
		this.usersQuantity = usersQuantity;
		this.ordersQuantity = ordersQuantity;
	}
}
