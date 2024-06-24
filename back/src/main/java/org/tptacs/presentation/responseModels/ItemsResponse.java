package org.tptacs.presentation.responseModels;

import java.util.List;
import java.util.stream.Collectors;

import org.tptacs.domain.entities.ItemOrder;
import org.tptacs.presentation.dto.ItemOrderDto;

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
