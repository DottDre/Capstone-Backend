package it.epicode.erboristeria.orders_item;

import com.fasterxml.jackson.annotation.JsonBackReference;
import it.epicode.erboristeria.Products.Product;

import it.epicode.erboristeria.orders.Order;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;


@Entity
@Table(name = "order_items")
@Data
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private int quantity;
    private BigDecimal price;

    public void setProductId(Long productId) {
    }
}
