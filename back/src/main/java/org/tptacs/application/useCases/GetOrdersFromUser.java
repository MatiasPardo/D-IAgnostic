package org.tptacs.application.useCases;

import org.springframework.stereotype.Service;
import org.tptacs.domain.entities.Order;
import org.tptacs.infraestructure.repositories.interfaces.IOrderRepository;
import org.tptacs.infraestructure.repositories.interfaces.IUserRepository;

import java.util.LinkedList;
import java.util.List;

@Service
public class GetOrdersFromUser {
    private final IOrderRepository orderRepository;
    private final IUserRepository userRepository;

    public GetOrdersFromUser(IOrderRepository orderRepository, IUserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    public List<Order> getOrdersFromUser(String userId) {
        return orderRepository.getOrdersFromUser(userId);
    }

	public List<Order> getOrdersFromUserInvited(String userName) {
		List<Order> orders = new LinkedList<Order>();
		List<String> orderId = userRepository.findByUsername(userName).get().getOrderShared();
		orderId.forEach(o-> orders.add(orderRepository.get(o)));
		return orders;
	}

}