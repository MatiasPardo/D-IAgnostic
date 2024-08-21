package org.diagnostic.application.useCases;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.diagnostic.domain.entities.Order;
import org.diagnostic.domain.entities.User;
import org.diagnostic.domain.enums.OrderStatus;
import org.diagnostic.domain.exceptions.AuthorizationException;
import org.diagnostic.domain.exceptions.NotFoundException;
import org.diagnostic.infraestructure.repositories.interfaces.IOrderRepository;
import org.diagnostic.infraestructure.repositories.interfaces.IUserRepository;

@Service
public class UpdateOrderUC {
    private final IOrderRepository orderRepository;
    private final IUserRepository userRepository;

	public UpdateOrderUC(IOrderRepository orderRepository, IUserRepository userRepository) {
		this.orderRepository = orderRepository;
		this.userRepository = userRepository;
	}

	public void updateStatusOrder(String orderId, String userId, OrderStatus status) {
		Order orderDB = orderRepository.get(orderId);
		if(!orderDB.getUserId().equals(userId)) throw new AuthorizationException("El usuario no esta autorizado para editar este pedido");
		orderDB.upateStatus(status);
		orderRepository.update(orderDB);
	}
	
	public Order updateOrderForShared(String orderId, String userName){
		Order orderDB = orderRepository.get(orderId);
		Optional<User> userInvited = userRepository.findByUsername(userName);
		if(userInvited.isEmpty()) throw new NotFoundException("User",userName);
		userInvited.get().addOrderShared(orderDB.getId());
		orderDB.addUserInvited(userInvited.get().getUsername());
		orderRepository.update(orderDB);
		userRepository.update(userInvited.get());
		return orderDB;
	}
}
