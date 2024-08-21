package org.diagnostic.application.useCases;

import org.springframework.stereotype.Service;
import org.diagnostic.domain.entities.ItemOrder;
import org.diagnostic.infraestructure.repositories.interfaces.IItemsRepository;
import org.diagnostic.infraestructure.repositories.interfaces.IOrderRepository;
import org.diagnostic.presentation.requestModels.ItemOrderRequest;

@Service
public class AddItemToOrderUC {
    private final IItemsRepository itemsRepository;
    private final IOrderRepository orderRepository;

    public AddItemToOrderUC(IItemsRepository itemsRepository, IOrderRepository orderRepository) {
        this.itemsRepository = itemsRepository;
        this.orderRepository = orderRepository;
    }

    public void addItemToOrder(String orderId, ItemOrderRequest orderRequest, String userId) {
        var order = this.orderRepository.get(orderId);

        var item = order.getItems().stream().filter(i -> sameItemOrder(orderRequest, i)).findFirst();
        item.ifPresentOrElse(
                existingItem -> existingItem.updateQuantity(orderRequest.getQuantity()),
                () -> {
                    var newItem = this.itemsRepository.get(orderRequest.getId());
                    var itemOrder = new ItemOrder(userId, newItem, orderRequest.getQuantity());
                    order.addItem(itemOrder);
                }
            );
        
        this.orderRepository.update(order);
    }

    private boolean sameItemOrder(ItemOrderRequest request, ItemOrder itemOrder) {
        return request.getId().equals(itemOrder.getItemId()) && request.getUserId().equals(itemOrder.getUserId());
    }
}