package com.pravin.shopping_cart.repositories;

import com.pravin.shopping_cart.entities.Profile;
import org.springframework.data.repository.CrudRepository;

public interface ProfileRepository extends CrudRepository<Profile, Long> {
}