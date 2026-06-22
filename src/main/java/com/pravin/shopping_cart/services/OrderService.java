package com.pravin.shopping_cart.services;

import com.pravin.shopping_cart.exceptions.OrderNotFountException;
import com.pravin.shopping_cart.repositories.OrderRepository;
import com.pravin.shopping_cart.dto.OrderDto;
import com.pravin.shopping_cart.mappers.OrderMapper;
import lombok.AllArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public List<OrderDto> getAllOrders(){
        var user = authService.getCurrentUser();
        var orders = orderRepository.getOrderByCustomer(user);
        return orders.stream().map(orderMapper::toDto).toList();
    }

    public OrderDto getOrder(Long orderId) {
        var order = orderRepository.getOrderWithItems(orderId).orElseThrow(OrderNotFountException::new);

        var user = authService.getCurrentUser();
        if(!order.isPlacedBy(user)) {
            throw new AccessDeniedException("you don't have access to this order");
        }
        return orderMapper.toDto(order);
    }
}
