package org.tptacs.presentation.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.tptacs.application.useCases.AddItemToOrderUC;
import org.tptacs.application.useCases.CreateOrderUC;
import org.tptacs.application.useCases.GetItemsFromOrderUC;
import org.tptacs.application.useCases.RemoveItemFromOrderUC;
import org.tptacs.application.useCases.UpdateItemOrderUC;
import org.tptacs.application.useCases.UpdateOrderUC;
import org.tptacs.domain.entities.Order;
import org.tptacs.domain.entities.User;
import org.tptacs.domain.enums.OrderStatus;
import org.tptacs.presentation.requestModels.ItemOrderRequest;
import org.tptacs.presentation.requestModels.OrderRequest;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(OrderController.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateOrderUC createOrderUC;

    @MockBean
    private AddItemToOrderUC addItemToOrderUC;

    @MockBean
    private GetItemsFromOrderUC getItemsFromOrderUC;
    
    @MockBean
    private UpdateOrderUC updateOrderUC;
    
    @MockBean
    private UpdateItemOrderUC updateItemOrderUC;
    
    @MockBean
    private RemoveItemFromOrderUC removeItemFromOrderUC;
    
    @MockBean
    private BaseController baseController;

    @BeforeEach
    void setUp() {
        Mockito.when(createOrderUC.createOrder(any(OrderRequest.class), any()))
        	.thenReturn(new Order("123","1","OrderName",List.of(),OrderStatus.NEW)); 
        Mockito.when(baseController.getUserFromJwt())
        	.thenReturn(new User("1","userTest","email@email.com","UnPasswordMuySeguro1234")); 
    }

    @Test
    public void testCreateOrderSuccess() throws Exception {
        OrderRequest request = new OrderRequest(List.of(new ItemOrderRequest("123",1L)));

        mockMvc.perform(MockMvcRequestBuilders
                .post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(403));
    }

    @Test
    public void testGetItemsSuccessNotAhutorized() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/orders/123/items")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(401));
    }
    
    @Test
    public void testUpdateOrderFail() throws Exception {
        String orderId = "order123";
        Long userId = 1L;

        mockMvc.perform(MockMvcRequestBuilders
                .patch("/api/orders/{orderId}/close/{userId}", orderId, userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(403));
    }


}
