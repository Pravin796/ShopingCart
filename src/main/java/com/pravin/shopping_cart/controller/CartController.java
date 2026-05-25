package com.pravin.shopping_cart.controller;

import com.pravin.shopping_cart.dto.CartDto;
import com.pravin.shopping_cart.entities.Cart;
import com.pravin.shopping_cart.mappers.CartMapper;
import com.pravin.shopping_cart.repositories.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@AllArgsConstructor
@RequestMapping("/carts")
public class CartController {
    private final CartMapper cartMapper;
    private final CartRepository cartRepository;

    @PostMapping
    public ResponseEntity<CartDto> createCart(
            UriComponentsBuilder uriBuilder
    ){
        var cart = new Cart();
        cartRepository.save(cart);

        var cartDto = cartMapper.toDto(cart);
        var uri = uriBuilder.path("/carts/{id}").buildAndExpand(cartDto.getId()).toUri();

        return ResponseEntity.created(uri).body(cartDto);
    }
}
