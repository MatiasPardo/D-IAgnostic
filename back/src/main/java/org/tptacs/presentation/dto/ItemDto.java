package org.tptacs.presentation.dto;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ItemDto {
	@JsonProperty("id")
    private String id;
	@JsonProperty("name")
    private String name;
	@JsonProperty("price")
    private BigDecimal price;
    @JsonProperty("picture")
    private String picture;
	
	@JsonCreator
	public ItemDto(String id, String name, BigDecimal price, String picture) {
		this.id = id;
		this.name = name;
		this.price = price;
        this.picture = picture;
	}
}
