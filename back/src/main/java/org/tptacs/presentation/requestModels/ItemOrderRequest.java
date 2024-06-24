package org.tptacs.presentation.requestModels;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemOrderRequest {
    private String id;
    private Long quantity;
    private String userId;
	
    public ItemOrderRequest(String id, Long quantity) {
        this.id = id;
        this.quantity = quantity;
    }
}
