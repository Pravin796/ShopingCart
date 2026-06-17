package com.pravin.shopping_cart.services;

import com.pravin.shopping_cart.entities.User;
import com.pravin.shopping_cart.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;

    public User getCurrentUser(){
        var authentication =  SecurityContextHolder.getContext().getAuthentication();
//        System.out.println(authentication);
        var userId = (Long) authentication.getPrincipal();

        return userRepository.findById(userId).orElse(null);
    }
}
