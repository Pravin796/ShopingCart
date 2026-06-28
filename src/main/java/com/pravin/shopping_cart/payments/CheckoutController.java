package com.pravin.shopping_cart.payments;

import com.pravin.shopping_cart.dto.ErrorDto;
import com.pravin.shopping_cart.exceptions.CartEmptyException;
import com.pravin.shopping_cart.exceptions.CartNotFoundException;
import com.pravin.shopping_cart.repositories.OrderRepository;
import jakarta.validation.Valid;
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
    private final OrderRepository orderRepository;

    @Value("${stripe.webhooksecretKey}")
    private String webhookSecretKey;

    @PostMapping
    public CheckoutResponse checkout(
            @Valid @RequestBody CheckoutRequest request
    ){
            return checkOutService.checkout(request);
    }

    @PostMapping("/webhook")
    public void handelWebHook(
            @RequestHeader Map<String, String> headers,
            @RequestBody String payload
    ){
        System.out.println("Webhook received");
        checkOutService.handelWebhookEvent(new webhookRequest(headers,payload));
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
