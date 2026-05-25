package com.pravin.shopping_cart.repositories;

import com.pravin.shopping_cart.entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
