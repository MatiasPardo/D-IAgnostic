package org.tptacs.application.useCases;

import org.springframework.stereotype.Service;
import org.tptacs.domain.entities.ItemOrder;
import org.tptacs.infraestructure.repositories.interfaces.IOrderRepository;

@Service
public class UpdateItemOrderUC {

    private final GetItemsFromOrderUC getItemsFromOrderUC;
	private final IOrderRepository orderRepository;

	public UpdateItemOrderUC(GetItemsFromOrderUC getItemsFromOrderUC, IOrderRepository orderRepository) {
		this.getItemsFromOrderUC = getItemsFromOrderUC;
		this.orderRepository = orderRepository;
	}
    
	public void updateItemOrder(String orderId, String itemId, Long quantity) {
		var order = this.orderRepository.get(orderId);
		var item = order.getItemFromOrder(itemId);
		item.updateQuantity(quantity);
		order.updateItemOrder(item);
		this.orderRepository.update(order);
	}
	
	    

}
