package org.tptacs.application.useCases;

import org.springframework.stereotype.Service;
import org.tptacs.infraestructure.repositories.interfaces.IItemsRepository;
import org.tptacs.infraestructure.repositories.interfaces.IOrderRepository;

@Service
public class RemoveItemFromOrderUC {

    private final IItemsRepository itemsRepository;
    private final IOrderRepository orderRepository;

    public RemoveItemFromOrderUC(IItemsRepository itemsRepository, IOrderRepository orderRepository) {
        this.itemsRepository = itemsRepository;
        this.orderRepository = orderRepository;
    }

    public void removeItemFromOrder(String orderId, String itemId, String userId) {
        var order = this.orderRepository.get(orderId);

        var item = this.itemsRepository.get(itemId);

        order.removeItem(item, userId);

        this.orderRepository.update(order);
    }

}
