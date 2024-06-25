package it.epicode.erboristeria.orders;

import it.epicode.erboristeria.orders_item.OrderItemResponseDTO;
import it.epicode.erboristeria.users.UserResponseDTO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class OrderResponseDTO {
    private Long id;
    private UserResponseDTO user;
    private LocalDate orderDate;
    private BigDecimal totalAmount;
    private List<OrderItemResponseDTO> orderItems;
}
