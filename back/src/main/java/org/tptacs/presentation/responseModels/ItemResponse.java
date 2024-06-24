package org.tptacs.presentation.responseModels;


import com.fasterxml.jackson.annotation.JsonProperty;

public class ItemResponse extends Response{

	@JsonProperty("ItemId")
	private String idItem;
	
	public ItemResponse(String idItem) {
		super();
		this.idItem = idItem;
	}

}
