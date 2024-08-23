package org.diagnostic.presentation.responseModels;

import java.util.List;
import java.util.stream.Collectors;

import org.diagnostic.domain.entities.ItemOrder;
import org.diagnostic.presentation.dto.ItemOrderDto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ItemsResponse extends Response{
	@JsonProperty("Items")
	private List<ItemOrderDto> items;

	@JsonCreator
	public ItemsResponse(List<ItemOrder> items) {
		super();
		this.items = items.stream().map(ItemOrder::toDto).collect(Collectors.toList());
	}
	
	
}
