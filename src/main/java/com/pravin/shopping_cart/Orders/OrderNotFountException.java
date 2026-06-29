package com.pravin.shopping_cart.Orders;

public class OrderNotFountException extends RuntimeException {
    public OrderNotFountException(){
        super("Order Not Found");
    }
}
