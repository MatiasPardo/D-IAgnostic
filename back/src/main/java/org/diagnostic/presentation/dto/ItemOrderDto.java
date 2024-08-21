package org.diagnostic.presentation.dto;


import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Data;

@Data
public class ItemOrderDto {
	private String id;
    private ItemDto item;
	private String userId;
	private Long total;
	private Long quantity;
	
	@JsonCreator
	public ItemOrderDto(String id, String userId, ItemDto item, Long quantity, Long total) {
		this.userId = userId;
		this.id = id;
		this.item = item;
		this.quantity = quantity;
		this.total = total;
	}
}
