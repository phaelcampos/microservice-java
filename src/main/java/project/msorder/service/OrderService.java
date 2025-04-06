package project.msorder.service;

import org.springframework.stereotype.Service;
import project.msorder.model.Order;
import project.msorder.model.OrderItem;
import project.msorder.repository.OrderRepository;

import java.util.List;

@Service
public class OrderService {
    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public Order save(Order order) {
        if (order.getItens() != null) {
            for (OrderItem item : order.getItens()) {
                item.setOrder(order);
            }
        }
        return orderRepository.save(order);
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }
}
