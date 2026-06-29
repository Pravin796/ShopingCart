package com.pravin.shopping_cart.payments;

import com.pravin.shopping_cart.Orders.OrderRepository;
import com.pravin.shopping_cart.Orders.Order;
import com.pravin.shopping_cart.carts.CartEmptyException;
import com.pravin.shopping_cart.carts.CartNotFoundException;
import com.pravin.shopping_cart.carts.CartRepository;
import com.pravin.shopping_cart.auth.AuthService;
import com.pravin.shopping_cart.carts.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class CheckOutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;


    @Transactional
    public CheckoutResponse checkout(CheckoutRequest request) {
        var cart = cartRepository.getCartWithItems(request.getCartId()).orElse(null);
        if(cart == null){
            throw new CartNotFoundException();
        }

        if(cart.isEmpty()){
            throw new CartEmptyException();
        }

        var order =  Order.fromCart(cart, authService.getCurrentUser());
        orderRepository.save(order);

        try{
            //        create a checkout session
            var session = paymentGateway.createCheckoutSessiosn(order);
            cartService.clearCart(cart.getId());
            return new CheckoutResponse(order.getId(), session.getCheckoutUrl());

        }catch(PaymentException ex){
            orderRepository.delete(order);
            throw ex;
        }
    }

    public void handelWebhookEvent(webhookRequest request){
        paymentGateway
                .parseWebhookRequest(request)
                .ifPresent( paymentResult ->{
                    var order = orderRepository.findById(paymentResult.getOrderId()).orElseThrow();
                    order.setStatus(paymentResult.getPaymentStatus());
                    orderRepository.save(order);
                });


    }
}