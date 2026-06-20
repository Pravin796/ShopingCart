package com.pravin.shopping_cart.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CheckoutRequest {
    @NotNull(message = "Cart id required")
    private UUID cartId;

    public UUID getCartId() {
        return cartId;
    }
}
