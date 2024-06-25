package it.epicode.erboristeria.orders;

import it.epicode.erboristeria.Products.Product;
import it.epicode.erboristeria.Products.ProductRepository;
import it.epicode.erboristeria.exception.ResourceNotFoundException;
import it.epicode.erboristeria.orders_item.OrderItem;
import it.epicode.erboristeria.orders_item.OrderItemRequestDTO;
import it.epicode.erboristeria.users.User;
import it.epicode.erboristeria.users.UserRepository;

import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
@Service
@Validated
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    public List<OrderResponseDTO> findAll() {
        return orderRepository.findAll().stream()
                .map(order -> {
                    OrderResponseDTO responseDto = new OrderResponseDTO();
                    BeanUtils.copyProperties(order, responseDto);
                    return responseDto;
                }).collect(Collectors.toList());
    }

    public OrderResponseDTO findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found for this id :: " + id));
        OrderResponseDTO responseDto = new OrderResponseDTO();
        BeanUtils.copyProperties(order, responseDto);
        return responseDto;
    }
    @Transactional
    public Order create(OrderRequestDTO orderRequestDTO) {
        Order order = new Order();


        User user = userRepository.findById(orderRequestDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        order.setUser(user);
        order.setOrderDate(LocalDate.now());

        List<OrderItem> orderItems = orderRequestDTO.getOrderItems().stream()
                .map(itemRequest -> {
                    OrderItem orderItem = new OrderItem();
                    orderItem.setProductId(itemRequest.getProductId());
                    orderItem.setQuantity(itemRequest.getQuantity());
                    Product product = productRepository.findById(itemRequest.getProductId())
                            .orElseThrow(() -> new IllegalArgumentException("Product not found"));
                    orderItem.setPrice(product.getPrice().multiply(new BigDecimal(itemRequest.getQuantity())));
                    orderItem.setOrder(order);
                    return orderItem;
                })
                .collect(Collectors.toList());

        BigDecimal totalAmount = orderItems.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);

        return orderRepository.save(order);
    }

    public OrderResponseDTO modify(Long id, @Valid OrderRequestDTO orderRequestDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found for this id :: " + id));
        BeanUtils.copyProperties(orderRequestDto, order);

        User user = userRepository.findById(orderRequestDto.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + orderRequestDto.getUserId()));
        order.setUser(user);

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequestDTO orderItemRequestDto : orderRequestDto.getOrderItems()) {
            OrderItem orderItem = new OrderItem();
            BeanUtils.copyProperties(orderItemRequestDto, orderItem);

            Product product = productRepository.findById(orderItemRequestDto.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + orderItemRequestDto.getProductId()));
            orderItem.setProduct(product);
            orderItem.setOrder(order);

            orderItems.add(orderItem);
        }
        order.setOrderItems(orderItems);

        orderRepository.save(order);
        OrderResponseDTO responseDto = new OrderResponseDTO();
        BeanUtils.copyProperties(order, responseDto);
        return responseDto;
    }

    public String delete(Long id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found for this id :: " + id);
        }
        orderRepository.deleteById(id);
        return "Order deleted";
    }
}
