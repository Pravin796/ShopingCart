package com.pravin.shopping_cart.controller;

import com.pravin.shopping_cart.dto.CheckoutRequest;
import com.pravin.shopping_cart.dto.CheckoutResponse;
import com.pravin.shopping_cart.dto.ErrorDto;
import com.pravin.shopping_cart.entities.Order;
import com.pravin.shopping_cart.entities.OrderItem;
import com.pravin.shopping_cart.entities.OrderStatus;
import com.pravin.shopping_cart.exceptions.CartEmptyException;
import com.pravin.shopping_cart.exceptions.CartNotFoundException;
import com.pravin.shopping_cart.exceptions.PaymentException;
import com.pravin.shopping_cart.repositories.CartRepository;
import com.pravin.shopping_cart.services.AuthService;
import com.pravin.shopping_cart.services.CartService;
import com.pravin.shopping_cart.services.CheckOutService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.net.Webhook;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/checkout")
public class CheckoutController {
    private final CheckOutService checkOutService;

    @Value("${stripe.webhookSecretKey}")
    private String webhookSecretKey;

    @PostMapping
    public CheckoutResponse checkout(
            @Valid @RequestBody CheckoutRequest request
    ){
            return checkOutService.checkout(request);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handelWebHook(
            @RequestHeader("Stripe-Signature") String signature,
            @RequestBody String payload
    ){
        try {
            var event = Webhook.constructEvent(payload, signature, webhookSecretKey);
            System.out.println(event.getType());

            var stripeObject = event.getDataObjectDeserializer().getObject().orElse(null);

            switch (event.getType()){
                case "payment_intent.succeeded" -> {

                }
                case "payment_intent.failed" -> {

                }
            }

            return ResponseEntity.ok().build();

        } catch (SignatureVerificationException e) {
            ResponseEntity.badRequest().build();
        }
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<?> handelPaymentException(){
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorDto("Error creating a chekout session"));
    }

    @ExceptionHandler({CartNotFoundException.class, CartEmptyException.class})
    public ResponseEntity<ErrorDto> handleException(Exception ex){
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }
}
