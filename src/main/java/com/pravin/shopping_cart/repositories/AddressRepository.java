package com.pravin.shopping_cart.repositories;


import com.pravin.shopping_cart.entities.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}