package project.msorder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import project.msorder.dto.OrderDTO;
import project.msorder.dto.OrderItemDTO;
import project.msorder.model.Order;
import project.msorder.model.OrderItem;
import project.msorder.service.OrderService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(OrderController.class)
@ActiveProfiles("test")
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @MockBean
    private RabbitTemplate rabbitTemplate;

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
    @DisplayName("Should save order and send to RabbitMQ successfully")
    void saveOrderSuccessfully() throws Exception {
        when(orderService.save(any(Order.class))).thenReturn(order);
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(OrderDTO.class));

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Pedido salvo e enviado para processamento")));

        verify(orderService, times(1)).save(any(Order.class));
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), any(OrderDTO.class));
    }

    @Test
    @DisplayName("Should save order without items successfully")
    void saveOrderWithoutItemsSuccessfully() throws Exception {
        Order orderWithoutItems = new Order();
        orderWithoutItems.setDescription("Order without items");
        when(orderService.save(any(Order.class))).thenReturn(orderWithoutItems);
        doNothing().when(rabbitTemplate).convertAndSend(anyString(), anyString(), any(OrderDTO.class));

        mockMvc.perform(post("/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderWithoutItems)))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Pedido salvo e enviado para processamento")));

        verify(orderService, times(1)).save(any(Order.class));
        verify(rabbitTemplate, times(1)).convertAndSend(anyString(), anyString(), any(OrderDTO.class));
    }

    @Test
    @DisplayName("Should find all orders successfully")
    void findAllOrdersSuccessfully() throws Exception {
        List<Order> orders = Arrays.asList(order);
        when(orderService.findAll()).thenReturn(orders);

        mockMvc.perform(get("/order"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description").value("Test Order"))
                .andExpect(jsonPath("$[0].itens[0].name").value("Item 1"))
                .andExpect(jsonPath("$[0].itens[0].quantity").value(2))
                .andExpect(jsonPath("$[0].itens[1].name").value("Item 2"))
                .andExpect(jsonPath("$[0].itens[1].quantity").value(3));

        verify(orderService, times(1)).findAll();
    }
} 