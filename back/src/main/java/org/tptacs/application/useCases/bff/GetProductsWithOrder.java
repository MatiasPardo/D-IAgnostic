package org.tptacs.application.useCases.bff;

import org.springframework.stereotype.Service;
import org.tptacs.domain.entities.ItemOrder;
import org.tptacs.infraestructure.repositories.interfaces.IItemsRepository;
import org.tptacs.infraestructure.repositories.interfaces.IOrderRepository;
import org.tptacs.presentation.dto.ItemOrderDto;
import java.util.List;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingLong;

@Service
public class GetProductsWithOrder {
    private final IItemsRepository itemsRepository;
    private final IOrderRepository orderRepository;

    public GetProductsWithOrder(IItemsRepository itemsRepository, IOrderRepository orderRepository) {
        this.itemsRepository = itemsRepository;
        this.orderRepository = orderRepository;
    }
    public List<ItemOrderDto> getProductsWithOrder(String orderId, String userId) {
        var items = itemsRepository.getAll();
        if (orderId == null) return items.stream().map(i -> new ItemOrderDto(null, null, i.toDto(), 0L, 0L)).toList();
        var itemsOrderDto = items.stream().map(i -> new ItemOrderDto(null, null, i.toDto(), 0L, 0L)).toList();

        var itemsInOrder = this.orderRepository.get(orderId).getItems();
        items.removeIf(i -> itemsInOrder.stream().map(ItemOrder::getItemId).toList().contains(i.getId()));

        var totalByItem = itemsInOrder.stream().collect(groupingBy(ItemOrder::getItemId, summingLong(ItemOrder::getQuantity)));

        var myItemsInOrder = itemsInOrder.stream().filter(i -> userId.equals(i.getUserId())).toList();
        itemsOrderDto.forEach(i -> {
            if (totalByItem.containsKey(i.getItem().getId())) {
                i.setTotal(totalByItem.get(i.getItem().getId()));
            }

            var item = myItemsInOrder.stream()
                    .filter(it -> it.getItem().getId().equals(i.getItem().getId())).findFirst().orElse(null);

            if(item != null) {
                i.setQuantity(item.getQuantity());
            }
        });
        return itemsOrderDto;
    }
}
