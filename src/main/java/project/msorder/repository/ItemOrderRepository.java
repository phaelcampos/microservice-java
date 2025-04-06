package project.msorder.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.msorder.model.OrderItem;

@Repository
public interface ItemOrderRepository extends JpaRepository<OrderItem, Long> {
}
