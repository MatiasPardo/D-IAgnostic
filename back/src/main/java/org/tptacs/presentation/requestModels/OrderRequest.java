package org.tptacs.presentation.requestModels;

import lombok.Getter;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Getter
public class OrderRequest {
	
	@JsonIgnore
    private String userId;
	@JsonProperty("items")
    private List<ItemOrderRequest> items;
	private String name;
	
	@JsonCreator
    public OrderRequest(
            @JsonProperty("items") List<ItemOrderRequest> items) {
        this.items = items;
    }

	public void assignUserId(String id) {
		this.userId = id;
	}
    
	
    
}
