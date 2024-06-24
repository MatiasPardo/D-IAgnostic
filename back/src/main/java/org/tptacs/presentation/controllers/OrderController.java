package org.tptacs.presentation.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.tptacs.application.useCases.*;
import org.tptacs.domain.entities.Order;
import org.tptacs.domain.enums.OrderStatus;
import org.tptacs.presentation.dto.OrderDto;
import org.tptacs.presentation.requestModels.ItemOrderRequest;
import org.tptacs.presentation.requestModels.OrderRequest;
import org.tptacs.presentation.requestModels.UpdateQuantity;
import org.tptacs.presentation.responseModels.*;

import io.swagger.v3.oas.annotations.tags.Tag;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Orders")
@RequestMapping(value = "/api/orders")
public class OrderController extends BaseController {

    private final CreateOrderUC createOrderUC;
    private final AddItemToOrderUC addItemToOrderUC;
    private final RemoveItemFromOrderUC removeItemFromOrderUC;
    private final GetItemsFromOrderUC getItemsFromOrderUC;
    private final UpdateOrderUC updateOrderUC;
    private final UpdateItemOrderUC updateItemOrderUC;
    private final GetOrdersFromUser getOrdersFromUser;

    public OrderController(CreateOrderUC createOrderUC,
                           AddItemToOrderUC addItemToOrderUC,
                           GetItemsFromOrderUC getItemsFromOrderUC,
                           UpdateOrderUC updateOrderUC,
                           RemoveItemFromOrderUC removeItemFromOrderUC,
                           UpdateItemOrderUC updateItemOrderUC,
                           GetOrdersFromUser getOrdersFromUser) {
        this.createOrderUC = createOrderUC;
        this.addItemToOrderUC = addItemToOrderUC;
        this.getItemsFromOrderUC = getItemsFromOrderUC;
        this.updateOrderUC = updateOrderUC;
        this.removeItemFromOrderUC = removeItemFromOrderUC;
        this.updateItemOrderUC = updateItemOrderUC;
        this.getOrdersFromUser = getOrdersFromUser;
    }


    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody OrderRequest orderRequest) {
    	orderRequest.assignUserId(this.getUserFromJwt().getId());
        Order order = createOrderUC.createOrder(orderRequest, getUserFromJwt().getId());
        URI location = URI.create("/items?order_id=" + order.getId());
        return ResponseEntity.created(location).body(new OrderResponse(order, "201", "Resource successfully created"));
    }

    @PostMapping(path = "/{orderId}/items", produces = "application/json", consumes = "application/json")
    public ResponseEntity<ItemResponse> createItem(@RequestBody ItemOrderRequest itemOrderRequest, @PathVariable("orderId") String orderID) {
        addItemToOrderUC.addItemToOrder(orderID, itemOrderRequest, getUserFromJwt().getId());
        return ResponseEntity.ok().body(new ItemResponse(itemOrderRequest.getId()));
    }

    @GetMapping(path = "/{orderId}/items", produces = "application/json")
    public ResponseEntity<ItemsResponse> getItems(@PathVariable("orderId") String orderId) {
        return ResponseEntity.ok().body(new ItemsResponse(getItemsFromOrderUC.getItemsFromOrder(orderId)));
    }

    @GetMapping(path = "", produces = "application/json")
    public ResponseEntity<OrdersResponse> getOrders() {
        List<Order> orders = getOrdersFromUser.getOrdersFromUser(this.getUserFromJwt().getId());
        List<Order> ordersShared = getOrdersFromUser.getOrdersFromUserInvited(this.getUserFromJwt().getUsername());
        orders.addAll(ordersShared);

        var mappedOrders = orders
                .stream()
                .map(order -> new OrderDto(order.getId(), order.getUserId(), order.getStatus().toString(), order.getItems().size() > 0, order.getName()))
                .collect(Collectors.toList());

        return ResponseEntity
                .ok()
                .body(new OrdersResponse(mappedOrders));
    }
    
	@PatchMapping(path = "/{orderId}",  produces = "application/json", consumes = "application/json")
    public ResponseEntity<Response> updateOrder(@PathVariable("orderId") String orderId) {
		updateOrderUC.updateStatusOrder(orderId, this.getUserFromJwt().getId(), OrderStatus.CLOSED);
		return ResponseEntity.ok().body(new Response());
    }
    
	@PatchMapping(path = "/{orderId}/users",  produces = "application/json", consumes = "application/json")
    public ResponseEntity<OrderResponse> updateOrderForShared(@PathVariable("orderId") String orderId) {
		return ResponseEntity.ok().body(new OrderResponse(updateOrderUC.updateOrderForShared(orderId, this.getUserFromJwt().getUsername())));
    }
	
	@PatchMapping(path = "/{orderId}/items/{itemId}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<Response> updateItemOrder(@RequestBody UpdateQuantity quantity, @PathVariable("orderId") String orderId, @PathVariable("itemId") String itemId) {
		updateItemOrderUC.updateItemOrder(orderId, itemId, quantity.getQuantity());
		return ResponseEntity.ok().body(new Response());
    }


    @DeleteMapping("/{orderId}/items/{itemId}")
    public ResponseEntity<Response> removeItem(@PathVariable("orderId") String orderID, @PathVariable("itemId") String itemID) {
        removeItemFromOrderUC.removeItemFromOrder(orderID, itemID, getUserFromJwt().getId());
		return ResponseEntity.ok().body(new Response());
    }

}
