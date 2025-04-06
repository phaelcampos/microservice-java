package project.msorder.dto;

import java.util.List;

public class OrderDTO {
    private Long id;
    private String description;
    private List<OrderItemDTO> itens; // Lista de DTOs de itens


    public OrderDTO() {
    }

    public OrderDTO(Long id, String description, List<OrderItemDTO> itens) {
        this.id = id;
        this.description = description;
        this.itens = itens;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<OrderItemDTO> getItens() {
        return itens;
    }

    public void setItens(List<OrderItemDTO> itens) {
        this.itens = itens;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", itens=" + (itens != null ? itens.size() + " items" : "null") +
                '}';
    }
}