package org.tptacs.domain.entities;

import java.math.BigDecimal;
import java.util.Objects;

import org.tptacs.presentation.dto.ItemDto;

import lombok.Getter;

@Getter
public class Item {
    private String id;
    private String name;
    private BigDecimal price;
    private String picture;

    public Item(String id, String name, BigDecimal price, String picture) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.picture = picture;
    }

    private Item() {}

	public ItemDto toDto() {
		ItemDto itemDto = new ItemDto(id, name, price, picture);
		return itemDto;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
