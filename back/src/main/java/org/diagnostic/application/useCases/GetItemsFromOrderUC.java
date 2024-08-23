package org.diagnostic.application.useCases;

import java.util.List;

import org.springframework.stereotype.Service;
import org.diagnostic.domain.entities.ItemOrder;
import org.diagnostic.infraestructure.repositories.interfaces.IOrderRepository;

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
