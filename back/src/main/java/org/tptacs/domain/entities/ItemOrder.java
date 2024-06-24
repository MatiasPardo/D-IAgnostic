package org.tptacs.domain.entities;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.tptacs.presentation.dto.ItemOrderDto;

import jakarta.validation.ValidationException;
import lombok.Getter;

@Getter
public class ItemOrder {
    private String id;
    private Item item;
    private Long quantity;
    private String userId;

    public ItemOrder(String userId, Item item, Long quantity) {
        this.id = UUID.randomUUID().toString();
        this.userId = userId;
        this.item = item;
        this.quantity = quantity;
    }

    private ItemOrder() {}

    @JsonIgnore
    public BigDecimal getPrice() {
        return this.item.getPrice().multiply(new BigDecimal(this.quantity));
    }
    
    public ItemOrderDto toDto() {
    	ItemOrderDto resItem = new ItemOrderDto(id, userId, item.toDto(), quantity, 0L);
    	return resItem;
    }

	public void updateQuantity(Long quantity) {
		if(quantity <= 0) throw new ValidationException("La cantidad debe ser mayor a 0");
		this.quantity = quantity;
	}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ItemOrder itemOrder = (ItemOrder) o;
        return (Objects.equals(this.item.getId(), itemOrder.getItem().getId()) && Objects.equals(this.userId, itemOrder.getUserId()))
                || Objects.equals(id, itemOrder.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(item);
    }

    @JsonIgnore
    public String getItemId() {
        return this.item.getId();
    }
}
