package com.pravin.shopping_cart.repositories;


import com.pravin.shopping_cart.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}