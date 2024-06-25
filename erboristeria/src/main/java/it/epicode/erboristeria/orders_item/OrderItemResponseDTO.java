package it.epicode.erboristeria.orders_item;

import it.epicode.erboristeria.Products.Product;
import it.epicode.erboristeria.Products.ProductResponseDTO;
import it.epicode.erboristeria.orders.Order;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderItemResponseDTO {
    private Long id;
    private ProductResponseDTO product;
    private int quantity;
    private BigDecimal price;

}
