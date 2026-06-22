package com.pravin.shopping_cart.exceptions;

public class OrderNotFountException extends RuntimeException {
    public OrderNotFountException(){
        super("Order Not Found");
    }
}
