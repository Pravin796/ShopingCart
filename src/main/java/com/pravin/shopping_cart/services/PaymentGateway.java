package com.pravin.shopping_cart.services;

import com.pravin.shopping_cart.entities.Order;

public interface PaymentGateway {
    CheckoutSession createCheckoutSessiosn(Order order);

}
