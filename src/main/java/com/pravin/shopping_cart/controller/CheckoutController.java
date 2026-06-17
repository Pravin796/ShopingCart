package com.pravin.shopping_cart.controller;

import com.pravin.shopping_cart.dto.CheckoutRequest;
import com.pravin.shopping_cart.dto.CheckoutResponse;
import com.pravin.shopping_cart.entities.Order;
import com.pravin.shopping_cart.entities.OrderItem;
import com.pravin.shopping_cart.entities.OrderStatus;
import com.pravin.shopping_cart.repositories.CartRepository;
import com.pravin.shopping_cart.services.AuthService;
import com.pravin.shopping_cart.services.CartService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/checkout")
public class CheckoutController {
    private final CartRepository cartRepository;
    private final AuthService authService;
    private final OrderRepository orderRepository;
    private final CartService cartService;

    @PostMapping
    public ResponseEntity<?> checkout(
          @Valid @RequestBody CheckoutRequest request
    ){
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if(cart == null){
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Cart not found")
            );
        }

        if(cart.getItems().isEmpty()){
            return ResponseEntity.badRequest().body(
                    Map.of("error", "Cart is empty")
            );
        }

        var order = new Order();
        order.setTotalPrice(cart.getTotalPrice());
        order.setStatus(OrderStatus.PENDING);
        order.setCustomer(authService.getCurrentUser());

        cart.getItems().forEach(items ->{
            var orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(items.getProduct());
            orderItem.setQuantity(items.getQuantity());
            orderItem.setTotalPrice(items.getToalPrice());
            orderItem.setUnitPrice(items.getProduct().getPrice());
            order.getItems().add(orderItem);
        });

        orderRepository.save(order);

        cartService.clearCart(cart.getId());

        return ResponseEntity.ok(new CheckoutResponse(order.getId()));
    }
}
