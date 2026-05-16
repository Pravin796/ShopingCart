package com.pravin.shopping_cart.repositories;


import com.pravin.shopping_cart.entities.Category;
import org.springframework.data.repository.CrudRepository;

public interface CategoryRepository extends CrudRepository<Category, Byte> {
}