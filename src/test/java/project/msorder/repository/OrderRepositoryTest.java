package project.msorder.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import project.msorder.dto.OrderDTO;
import project.msorder.model.Order;
import project.msorder.model.OrderItem;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class OrderRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Test
    @DisplayName("Should get order")
    void findOrderSuccessfully() {
        Order order = new Order();
        order.setDescription("Test Order");

        OrderItem item1 = new OrderItem();
        item1.setName("Item 1");
        item1.setQuantity(2);
        item1.setOrder(order);

        OrderItem item2 = new OrderItem();
        item2.setName("Item 2");
        item2.setQuantity(3);
        item2.setOrder(order);

        order.setItens(Arrays.asList(item1, item2));

        Order savedOrder = createOrder(order);

        entityManager.clear();

        Order foundOrder = entityManager.find(Order.class, savedOrder.getId());

        assertNotNull(foundOrder);
        assertEquals("Test Order", foundOrder.getDescription());
        assertEquals(2, foundOrder.getItens().size());
        
        List<OrderItem> items = foundOrder.getItens();
        assertEquals("Item 1", items.get(0).getName());
        assertEquals(2, items.get(0).getQuantity());
        assertEquals("Item 2", items.get(1).getName());
        assertEquals(3, items.get(1).getQuantity());
    }

    @Test
    @DisplayName("Should find all orders")
    void findAllOrdersSuccessfully() {
        Order order1 = new Order();
        order1.setDescription("First Order");
        OrderItem item1 = new OrderItem();
        item1.setName("Item 1");
        item1.setQuantity(1);
        item1.setOrder(order1);
        order1.setItens(Arrays.asList(item1));
        createOrder(order1);

        Order order2 = new Order();
        order2.setDescription("Second Order");
        OrderItem item2 = new OrderItem();
        item2.setName("Item 2");
        item2.setQuantity(2);
        item2.setOrder(order2);
        order2.setItens(Arrays.asList(item2));
        createOrder(order2);

        entityManager.clear();

        List<Order> orders = entityManager.createQuery("SELECT o FROM Order o", Order.class).getResultList();

        assertNotNull(orders);
        assertEquals(2, orders.size());
        
        Order foundOrder1 = orders.stream()
                .filter(o -> o.getDescription().equals("First Order"))
                .findFirst()
                .orElse(null);
        assertNotNull(foundOrder1);
        assertEquals(1, foundOrder1.getItens().size());
        assertEquals("Item 1", foundOrder1.getItens().get(0).getName());
        assertEquals(1, foundOrder1.getItens().get(0).getQuantity());

        Order foundOrder2 = orders.stream()
                .filter(o -> o.getDescription().equals("Second Order"))
                .findFirst()
                .orElse(null);
        assertNotNull(foundOrder2);
        assertEquals(1, foundOrder2.getItens().size());
        assertEquals("Item 2", foundOrder2.getItens().get(0).getName());
        assertEquals(2, foundOrder2.getItens().get(0).getQuantity());
    }

    private Order createOrder(Order order) {
        this.entityManager.persist(order);
        return order;
    }
}