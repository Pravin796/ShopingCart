package com.pravin.shopping_cart.controller;

import com.pravin.shopping_cart.dto.AddItemToCartRequest;
import com.pravin.shopping_cart.dto.CartDto;
import com.pravin.shopping_cart.dto.CartItemDto;
import com.pravin.shopping_cart.dto.UpdateCartItemRequest;
import com.pravin.shopping_cart.entities.Cart;
import com.pravin.shopping_cart.entities.CartItem;
import com.pravin.shopping_cart.mappers.CartMapper;
import com.pravin.shopping_cart.repositories.CartRepository;
import com.pravin.shopping_cart.repositories.ProductRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/carts")
public class CartController {
    private final CartMapper cartMapper;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;

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

    @PostMapping("/{cartId}/items")
    public ResponseEntity<CartItemDto> addToCart(
            @PathVariable UUID cartId,
            @RequestBody AddItemToCartRequest request
    ){
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if(cart == null) {
            return ResponseEntity.notFound().build();
        }

        var product = productRepository.findById(request.getProductId()).orElse(null);

        if(product == null){
            return ResponseEntity.badRequest().build();
        }

        var cartItem =  cart.addItem(product);

        cartRepository.save(cart);
        var cartItemdto = cartMapper.toDto(cartItem);
        return ResponseEntity.status(HttpStatus.CREATED).body(cartItemdto);

    }

    @GetMapping("/{cartId}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID cartId){
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if(cart == null){
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(cartMapper.toDto(cart));
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateItem(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId,
            @Valid @RequestBody UpdateCartItemRequest request
    ){
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if(cart == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Cart not found")
            );
        }

        var cartItem = cart.getItem(productId);
        if(cartItem == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Product was not found in the cart")
            );
        }

        cartItem.setQuantity(request.getQuantity());
        cartRepository.save(cart);

        return ResponseEntity.ok(cartMapper.toDto(cartItem));
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> removeItem(
            @PathVariable("cartId") UUID cartId,
            @PathVariable("productId") Long productId
    ){
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);
        if(cart == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    Map.of("error", "Cart was not found")
            );
        }

        cart.removeItem(productId);
        cartRepository.save(cart);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<Void> clearCart(
            @PathVariable UUID cartId
    ){
        var cart = cartRepository.getCartWithItems(cartId).orElse(null);

        if(cart == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        cart.clear();

        cartRepository.save(cart);

        return ResponseEntity.noContent().build();
    }

}
