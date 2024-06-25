package it.epicode.erboristeria.orders;

import it.epicode.erboristeria.orders_item.OrderItemRequestDTO;
import it.epicode.erboristeria.users.User;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrderRequestDTO {
    private Long userId;
    private List<OrderItemRequestDTO> orderItems;
}
