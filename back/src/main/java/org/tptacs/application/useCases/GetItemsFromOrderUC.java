package org.tptacs.application.useCases;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.tptacs.domain.entities.ItemOrder;
import org.tptacs.infraestructure.repositories.interfaces.IOrderRepository;

import jakarta.validation.ValidationException;

@Service
public class GetItemsFromOrderUC {
    private final IOrderRepository orderRepository;

    public GetItemsFromOrderUC(IOrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<ItemOrder> getItemsFromOrder(String orderId) {
        var order = this.orderRepository.get(orderId);
        return order.getItems();
    }
}
