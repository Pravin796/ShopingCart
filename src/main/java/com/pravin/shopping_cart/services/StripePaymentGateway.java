package com.pravin.shopping_cart.services;

import com.pravin.shopping_cart.entities.Order;
import com.pravin.shopping_cart.entities.OrderItem;
import com.pravin.shopping_cart.exceptions.PaymentException;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class StripePaymentGateway implements PaymentGateway {

    @Value("${websiteUrl}")
    private String websiteUrl;

    @Override
    public CheckoutSession createCheckoutSessiosn(Order order) {
        try {
            var builder = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(websiteUrl + "/checkout-success?orderId=" + order.getId())
                    .setCancelUrl(websiteUrl + "/checkout-cancel")
                    .putMetadata("order_id", order.getId().toString());

            order.getItems().forEach(items -> {
                var LineItem = createLineItem(items);
                builder.addLineItem(LineItem);
            });

            var session = Session.create(builder.build());
            return new CheckoutSession(session.getUrl());
        } catch (StripeException ex) {
            System.out.println(ex.getMessage());
            throw new PaymentException();
        }
    }

    private SessionCreateParams.LineItem createLineItem(OrderItem items) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(Long.valueOf(items.getQuantity()))
                .setPriceData(createPriceData(items))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(OrderItem items) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("usd")
                .setUnitAmountDecimal(items.getUnitPrice().multiply(BigDecimal.valueOf(100)))
                .setProductData(createProductData(items))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData.ProductData createProductData(OrderItem items) {
        return SessionCreateParams.LineItem.PriceData.ProductData.builder()
                .setName(items.getProduct().getName())
                .build();
    }

}
