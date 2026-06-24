package com.pravin.shopping_cart.controller;

import com.pravin.shopping_cart.dto.CheckoutRequest;
import com.pravin.shopping_cart.dto.CheckoutResponse;
import com.pravin.shopping_cart.dto.ErrorDto;
import com.pravin.shopping_cart.entities.Order;
import com.pravin.shopping_cart.entities.OrderItem;
import com.pravin.shopping_cart.entities.OrderStatus;
import com.pravin.shopping_cart.exceptions.CartEmptyException;
import com.pravin.shopping_cart.exceptions.CartNotFoundException;
import com.pravin.shopping_cart.repositories.CartRepository;
import com.pravin.shopping_cart.services.AuthService;
import com.pravin.shopping_cart.services.CartService;
import com.pravin.shopping_cart.services.CheckOutService;
import com.stripe.exception.StripeException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/checkout")
public class CheckoutController {
    private final CheckOutService checkOutService;

    @PostMapping
    public ResponseEntity<?> checkout(
            @Valid @RequestBody CheckoutRequest request
    ){
        try{
            return ResponseEntity.ok(checkOutService.checkout(request));
        }catch (StripeException ex){
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorDto("Error creating a checkout session"));
        }
    }

    @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
    public ResponseEntity<ErrorDto> handleException(Exception ex){
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }
}
