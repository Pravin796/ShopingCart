package com.pravin.shopping_cart.controller;

import com.pravin.shopping_cart.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}