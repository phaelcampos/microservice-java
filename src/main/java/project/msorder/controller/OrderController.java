package project.msorder.controller;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import project.msorder.dto.OrderDTO;
import project.msorder.dto.OrderItemDTO;
import project.msorder.model.Order;
import project.msorder.model.OrderItem;
import project.msorder.service.OrderService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.Collections;

@RestController
@RequestMapping("/order")
public class OrderController {
    private final RabbitTemplate rabbitTemplate;
    private OrderService orderService;

    public OrderController(RabbitTemplate rabbitTemplate,OrderService orderService) {
        this.orderService = orderService;
        this.rabbitTemplate = rabbitTemplate;
    }
    @Value("${broker.queue.processing.name}")
    private String routingKey;

    @PostMapping
    public String saveOrder(@RequestBody Order order) {
        if (order.getItens() != null) {
            for (OrderItem item : order.getItens()) {
                item.setOrder(order);
            }
        }
        Order savedOrder = orderService.save(order);

        List<OrderItemDTO> itemDTOs;
        if (savedOrder.getItens() != null) {
            itemDTOs = savedOrder.getItens().stream()
                    .map(itemEntity -> new OrderItemDTO(
                            itemEntity.getName(),
                            itemEntity.getQuantity()
                    ))
                    .collect(Collectors.toList());
        } else {
            itemDTOs = Collections.emptyList();
        }

        OrderDTO orderDtoToSend = new OrderDTO(
                savedOrder.getId(),
                savedOrder.getDescription(),
                itemDTOs
        );

        rabbitTemplate.convertAndSend("", routingKey, orderDtoToSend);
        System.out.println("Enviando para RabbitMQ: " + orderDtoToSend); // Log para depuração

        return "Pedido salvo e enviado para processamento " + savedOrder.getDescription();
    }

    @GetMapping
    public List<Order> findAll() {
        return orderService.findAll();
    }
}
