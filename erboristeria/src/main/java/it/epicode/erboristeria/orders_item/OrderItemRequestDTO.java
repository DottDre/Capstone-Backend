package it.epicode.erboristeria.orders_item;

import it.epicode.erboristeria.Products.Product;
import it.epicode.erboristeria.orders.Order;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemRequestDTO {
    private Long productId;
    private int quantity;
}
