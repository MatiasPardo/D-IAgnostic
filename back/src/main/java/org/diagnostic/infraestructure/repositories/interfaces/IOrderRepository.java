package org.diagnostic.infraestructure.repositories.interfaces;

import org.diagnostic.domain.entities.Order;

import java.util.List;

public interface IOrderRepository {
    void save(Order order);
    Order get(String id);
    void exists(String id);
    void update(Order order);
    Long count();
    List<Order> getOrdersFromUser(String userId);
}
