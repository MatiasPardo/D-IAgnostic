package org.tptacs.application.usesCases;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.tptacs.application.useCases.UpdateOrderUC;
import org.tptacs.domain.entities.Item;
import org.tptacs.domain.entities.ItemOrder;
import org.tptacs.domain.entities.Order;
import org.tptacs.domain.enums.OrderStatus;
import org.tptacs.infraestructure.repositories.interfaces.IOrderRepository;
import org.tptacs.infraestructure.repositories.interfaces.IUserRepository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.List;

public class UpdateOrderUCTest {

    private UpdateOrderUC updateOrderUC;
    
    @Mock
    private IUserRepository userRepository;

    @Mock
    private IOrderRepository orderRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        updateOrderUC = new UpdateOrderUC(orderRepository, userRepository);

    }

    @Test
    @Disabled
    public void testUpdateStatusOrderWithValidData() {
    	Order orderDB = new Order("order123", "1", "", List.of(new ItemOrder("", new Item("abc","name",new BigDecimal(100), ""),1L)),OrderStatus.NEW);

        when(orderRepository.get("order123")).thenReturn(orderDB);

        updateOrderUC.updateStatusOrder("order123", "1", OrderStatus.CLOSED);

        assertThat(orderDB.getStatus()).isEqualTo(OrderStatus.CLOSED);

        verify(orderRepository, times(1)).save(orderDB);
    }

    @Test
    @Disabled
    public void testUpdateStatusOrderWithInvalidData() {
    	Order orderDB = new Order("order1235", "1", "", List.of(new ItemOrder("", new Item("abc","name",new BigDecimal(100), ""),1L)),OrderStatus.NEW);
    	
        when(orderRepository.get("order1235")).thenReturn(orderDB);

        updateOrderUC.updateStatusOrder("order1235", "2", OrderStatus.CLOSED);

        assertThat(orderDB.getStatus()).isEqualTo(OrderStatus.NEW); // Estado no se ha actualizado

        verify(orderRepository, never()).save(orderDB);
    }

    @Test
    @Disabled
    public void testUpdateStatusOrderWithNonExistingOrder() {
    	Order orderDB = new Order("order123", "1", "", List.of(new ItemOrder("", new Item("abc","name",new BigDecimal(100), ""),1L)),OrderStatus.NEW);
    
        when(orderRepository.get("order123")).thenReturn(null);

        updateOrderUC.updateStatusOrder("order123", "1", OrderStatus.CLOSED);
        
        assertThat(orderDB.getStatus()).isEqualTo(OrderStatus.NEW); // Estado no se ha actualizado

        verify(orderRepository, never()).save(any(Order.class));
    }
}
