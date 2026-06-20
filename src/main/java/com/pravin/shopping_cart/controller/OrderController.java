package com.pravin.shopping_cart.controller;

import com.pravin.shopping_cart.dto.OrderDto;
import com.pravin.shopping_cart.mappers.OrderMapper;
import com.pravin.shopping_cart.repositories.OrderRepository;
import com.pravin.shopping_cart.services.AuthService;
import com.pravin.shopping_cart.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public List<OrderDto> getAllOrders(){
        return orderService.getAllOrders();
    }

}
