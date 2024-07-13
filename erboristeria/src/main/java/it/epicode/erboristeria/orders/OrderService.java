package it.epicode.erboristeria.orders;

import it.epicode.erboristeria.Products.Product;
import it.epicode.erboristeria.Products.ProductRepository;
import it.epicode.erboristeria.Products.ProductResponseDTO;
import it.epicode.erboristeria.exception.ResourceNotFoundException;
import it.epicode.erboristeria.orders_item.OrderItem;
import it.epicode.erboristeria.orders_item.OrderItemRequestDTO;
import it.epicode.erboristeria.orders_item.OrderItemResponseDTO;
import it.epicode.erboristeria.users.User;
import it.epicode.erboristeria.users.UserRepository;

import it.epicode.erboristeria.users.UserResponseDTO;
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

                    if (order.getUser() != null) {
                        UserResponseDTO userResponseDTO = new UserResponseDTO();
                        BeanUtils.copyProperties(order.getUser(), userResponseDTO);
                        responseDto.setUser(userResponseDTO);
                    }

                    if (order.getOrderItems() != null) {
                        List<OrderItemResponseDTO> orderItemResponseDTOs = order.getOrderItems().stream()
                                .map(orderItem -> {
                                    OrderItemResponseDTO orderItemResponseDTO = new OrderItemResponseDTO();
                                    BeanUtils.copyProperties(orderItem, orderItemResponseDTO);

                                    if (orderItem.getProduct() != null) {
                                        ProductResponseDTO productResponseDTO = new ProductResponseDTO();
                                        BeanUtils.copyProperties(orderItem.getProduct(), productResponseDTO);
                                        orderItemResponseDTO.setProduct(productResponseDTO);
                                    }

                                    return orderItemResponseDTO;
                                })
                                .collect(Collectors.toList());
                        responseDto.setOrderItems(orderItemResponseDTOs);
                    }

                    return responseDto;
                }).collect(Collectors.toList());
    }

    public OrderResponseDTO findById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found for this id :: " + id));
        OrderResponseDTO responseDto = new OrderResponseDTO();
        BeanUtils.copyProperties(order, responseDto);

        if (order.getUser() != null) {
            UserResponseDTO userResponseDTO = new UserResponseDTO();
            BeanUtils.copyProperties(order.getUser(), userResponseDTO);
            responseDto.setUser(userResponseDTO);
        }

        if (order.getOrderItems() != null) {
            List<OrderItemResponseDTO> orderItemResponseDTOs = order.getOrderItems().stream()
                    .map(orderItem -> {
                        OrderItemResponseDTO orderItemResponseDTO = new OrderItemResponseDTO();
                        BeanUtils.copyProperties(orderItem, orderItemResponseDTO);

                        if (orderItem.getProduct() != null) {
                            ProductResponseDTO productResponseDTO = new ProductResponseDTO();
                            BeanUtils.copyProperties(orderItem.getProduct(), productResponseDTO);
                            orderItemResponseDTO.setProduct(productResponseDTO);
                        }

                        return orderItemResponseDTO;
                    })
                    .collect(Collectors.toList());
            responseDto.setOrderItems(orderItemResponseDTOs);
        }

        return responseDto;
    }

    @Transactional
    public Order create(OrderRequestDTO orderRequestDTO) {
        Order order = new Order();

        User user = userRepository.findById(orderRequestDTO.getUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + orderRequestDTO.getUserId()));
        order.setUser(user);
        order.setOrderDate(LocalDate.now());

        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemRequestDTO itemRequest : orderRequestDTO.getOrderItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new ResourceNotFoundException("Product not found for this id :: " + itemRequest.getProductId()));

            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(product);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(product.getPrice().multiply(new BigDecimal(itemRequest.getQuantity())));
            orderItem.setOrder(order);
            orderItems.add(orderItem);
        }

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

        if (order.getUser() != null) {
            UserResponseDTO userResponseDTO = new UserResponseDTO();
            BeanUtils.copyProperties(order.getUser(), userResponseDTO);
            responseDto.setUser(userResponseDTO);
        }

        if (order.getOrderItems() != null) {
            List<OrderItemResponseDTO> orderItemResponseDTOs = order.getOrderItems().stream()
                    .map(orderItem -> {
                        OrderItemResponseDTO orderItemResponseDTO = new OrderItemResponseDTO();
                        BeanUtils.copyProperties(orderItem, orderItemResponseDTO);

                        if (orderItem.getProduct() != null) {
                            ProductResponseDTO productResponseDTO = new ProductResponseDTO();
                            BeanUtils.copyProperties(orderItem.getProduct(), productResponseDTO);
                            orderItemResponseDTO.setProduct(productResponseDTO);
                        }

                        return orderItemResponseDTO;
                    })
                    .collect(Collectors.toList());
            responseDto.setOrderItems(orderItemResponseDTOs);
        }

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
