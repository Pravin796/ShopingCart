package com.pravin.shopping_cart.services;

import com.pravin.shopping_cart.repositories.OrderRepository;
import com.pravin.shopping_cart.dto.CheckoutRequest;
import com.pravin.shopping_cart.dto.CheckoutResponse;
import com.pravin.shopping_cart.entities.Order;
import com.pravin.shopping_cart.exceptions.CartEmptyException;
import com.pravin.shopping_cart.exceptions.CartNotFoundException;
import com.pravin.shopping_cart.repositories.CartRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CheckOutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;

    public CheckoutResponse checkout(CheckoutRequest request){
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if(cart == null){
            throw new CartNotFoundException();
        }

        if(cart.isEmpty()){
            throw new CartEmptyException();
        }

        var order =  Order.fromCart(cart, authService.getCurrentUser());
        orderRepository.save(order);

        cartService.clearCart(cart.getId());
        return new CheckoutResponse(order.getId());
    }

}
