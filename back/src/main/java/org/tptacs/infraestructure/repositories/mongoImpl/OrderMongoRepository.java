package org.tptacs.infraestructure.repositories.mongoImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import org.tptacs.domain.entities.Order;
import org.tptacs.domain.exceptions.NotFoundException;
import org.tptacs.infraestructure.repositories.interfaces.IOrderRepository;
import org.tptacs.infraestructure.repositories.mongoImpl.interfaces.IOrderMongoRepository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@Primary
public class OrderMongoRepository implements IOrderRepository {
    @Autowired
    private IOrderMongoRepository IOrderMongoRepository;

    public void save(Order order) {
        IOrderMongoRepository.save(order);
    }

    public Order get(String id) {
        var item = IOrderMongoRepository.findById(id);
        if (item.isEmpty()) throw new NotFoundException(id, "pedido");
        return item.get();
    }

    public void update(Order order){
        IOrderMongoRepository.save(order);
    }

    @Override
    public void exists(String id) {
        if (!IOrderMongoRepository.existsById(id)) throw new NotFoundException(id, "pedido");
    }

    public Long count() {
    	return IOrderMongoRepository.count();
    }

    // Mejorar este método para no traerse todos los objetos y filtrar a memoria
    @Override
    public List<Order> getOrdersFromUser(String userId) {
        return IOrderMongoRepository.findAll()
                .stream().filter(order -> order.getUserId().equals(userId)).collect(Collectors.toList());
    }

}
