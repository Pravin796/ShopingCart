package com.pravin.shopping_cart.payments;

import com.pravin.shopping_cart.Orders.Order;
import com.pravin.shopping_cart.Orders.OrderItem;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.PaymentIntent;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class StripePaymentGateway implements PaymentGateway {

    @Value("${websiteUrl}")
    private String websiteUrl;

    @Value("${stripe.webhooksecretKey}")
    private String webhookSecretKey;

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

//    @Override
//    public Optional<PaymentResult> parseWebhookRequest(webhookRequest request) {
//        try {
//            var payload = request.getPayload();
//            var signature = request.getHeaders().get("Stripe-Signature");
//            var event = Webhook.constructEvent(payload, signature, webhookSecretKey);
//
//            // Print event type
//            System.out.println("Event Type = " + event.getType());
//
//            return switch (event.getType()){
//                case "checkout.session.completed" ->
//                        Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.PAID));
//
//                case "checkout.session.expired" ->
//                    Optional.of(new PaymentResult(extractOrderId(event), PaymentStatus.FAILED));
//
//                default -> Optional.empty();
//            };
//
//        } catch (SignatureVerificationException e) {
//            throw new PaymentException("Invalid signature");
//        }
//    }
@Override
public Optional<PaymentResult> parseWebhookRequest(webhookRequest request) {
    try {
        var payload = request.getPayload();
        var signature = request.getHeaders().get("Stripe-Signature");
        var event = Webhook.constructEvent(payload, signature, webhookSecretKey);

        // Print event type
        System.out.println("Event Type = " + event.getType());

        // Only for debugging payment_intent events
        if (event.getType().equals("payment_intent.succeeded")) {

            var stripeObject = event.getDataObjectDeserializer()
                    .getObject()
                    .orElseThrow();

            PaymentIntent paymentIntent = (PaymentIntent) stripeObject;

            System.out.println("Metadata = " + paymentIntent.getMetadata());
            System.out.println("Order ID = " + paymentIntent.getMetadata().get("order_id"));
        }

        return switch (event.getType()) {

            case "checkout.session.completed" ->
                    Optional.of(new PaymentResult(
                            extractOrderIdFromSession(event),
                            PaymentStatus.PAID));

            default -> Optional.empty();
        };

    } catch (SignatureVerificationException e) {
        throw new PaymentException("Invalid signature");
    }
}

    private Long extractOrderIdFromSession(Event event) {
        Session session = (Session) event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() -> new PaymentException("Cannot deserialize session"));

        System.out.println("Session Metadata = " + session.getMetadata());
        return Long.valueOf(session.getMetadata().get("order_id"));
    }

//    private Long extractOrderId(Event event) {
//        Session session = (Session) event.getDataObjectDeserializer()
//                .getObject()
//                .orElseThrow(() ->
//                        new PaymentException("Cannot deserialize session"));
//        return Long.valueOf(session.getMetadata().get("order_id"));
//    }

    private SessionCreateParams.LineItem createLineItem(OrderItem items) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(Long.valueOf(items.getQuantity()))
                .setPriceData(createPriceData(items))
                .build();
    }

    private SessionCreateParams.LineItem.PriceData createPriceData(OrderItem items) {
        return SessionCreateParams.LineItem.PriceData.builder()
                .setCurrency("inr")
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
