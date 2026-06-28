package com.pravin.shopping_cart.payments;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@AllArgsConstructor
@Getter
public class webhookRequest {
    private Map<String, String> headers;
    private String payload;
}
