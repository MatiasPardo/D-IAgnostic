package org.tptacs.application.dto;

import org.tptacs.domain.entities.Item;
import org.tptacs.domain.entities.Order;

import java.math.BigDecimal;

public class ItemsWithOrderDTO {
    private String id;
    private String name;
    private BigDecimal price;
    private Long quantity;
    private String picture;

    public ItemsWithOrderDTO(Long quantity, Item item) {
        this.id = item.getId();
        this.name = item.getName();
        this.price = item.getPrice();
        this.quantity = quantity;
        this.picture = item.getPicture();
    }
}
