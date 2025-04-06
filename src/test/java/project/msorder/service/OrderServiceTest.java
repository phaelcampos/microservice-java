package project.msorder.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import project.msorder.model.Order;
import project.msorder.model.OrderItem;
import project.msorder.repository.OrderRepository;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private Order order;
    private OrderItem item1;
    private OrderItem item2;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setDescription("Test Order");

        item1 = new OrderItem();
        item1.setName("Item 1");
        item1.setQuantity(2);

        item2 = new OrderItem();
        item2.setName("Item 2");
        item2.setQuantity(3);

        order.setItens(Arrays.asList(item1, item2));
    }

    @Test
    @DisplayName("Should save order with items successfully")
    void saveOrderSuccessfully() {
        when(orderRepository.save(any(Order.class))).thenReturn(order);

        Order savedOrder = orderService.save(order);

        assertNotNull(savedOrder);
        assertEquals("Test Order", savedOrder.getDescription());
        assertEquals(2, savedOrder.getItens().size());
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    @DisplayName("Should save order without items successfully")
    void saveOrderWithoutItemsSuccessfully() {
        Order orderWithoutItems = new Order();
        orderWithoutItems.setDescription("Order without items");
        when(orderRepository.save(any(Order.class))).thenReturn(orderWithoutItems);

        Order savedOrder = orderService.save(orderWithoutItems);

        assertNotNull(savedOrder);
        assertEquals("Order without items", savedOrder.getDescription());
        assertNull(savedOrder.getItens());
        verify(orderRepository, times(1)).save(orderWithoutItems);
    }

    @Test
    @DisplayName("Should find all orders successfully")
    void findAllOrdersSuccessfully() {

        List<Order> orders = Arrays.asList(order);
        when(orderRepository.findAll()).thenReturn(orders);


        List<Order> foundOrders = orderService.findAll();
        assertNotNull(foundOrders);
        assertEquals(1, foundOrders.size());
        assertEquals("Test Order", foundOrders.get(0).getDescription());
        verify(orderRepository, times(1)).findAll();
    }
} 