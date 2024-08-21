package org.diagnostic.infraestructure.repositories;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Repository;
import org.diagnostic.domain.entities.Order;
import org.diagnostic.domain.exceptions.NotFoundException;
import org.diagnostic.infraestructure.repositories.interfaces.IOrderRepository;

@Repository
public class OrderRepository extends FileRepository<Order> implements IOrderRepository {
    public OrderRepository() {
        super(OrderRepository.class.getSimpleName(), Order.class);
    }

    public void save(Order order) {
        super.put(order.getId(), order);
    }

    public Order get(String id) {
        var item = super.get(id);
        if (item == null) throw new NotFoundException(id, "pedido");
        return super.get(id);
    }

    public void update(Order order){
        super.replace(order.getId(), order);
    }

    @Override
    public void exists(String id) {
        if (!super.exist(id)) throw new NotFoundException(id, "pedido");
    }
    
    public Long count() {
    	return super.count();
    }

    @Override
    public List<Order> getOrdersFromUser(String userId) {
        return values().stream().filter(order -> order.getUserId().equals(userId)).collect(Collectors.toList());
    }

}
