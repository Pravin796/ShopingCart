package com.pravin.shopping_cart.payments;

import com.pravin.shopping_cart.entities.Order;

import java.util.Optional;

public interface PaymentGateway {
    CheckoutSession createCheckoutSessiosn(Order order);
    Optional<PaymentResult> parseWebhookRequest(webhookRequest request);
}
