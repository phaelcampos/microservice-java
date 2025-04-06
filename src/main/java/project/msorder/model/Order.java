package project.msorder.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private String description;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<OrderItem> itens;

    public Order(String description, List<OrderItem> itens) {
        this.description = description;
        this.itens = itens;
    }

    public Order() {

    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setItens(List<OrderItem> itens) {
        this.itens = itens;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public List<OrderItem> getItens() {
        return itens;
    }
}
