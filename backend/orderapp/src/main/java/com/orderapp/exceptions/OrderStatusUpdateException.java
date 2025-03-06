package com.orderapp.exceptions;

public class OrderStatusUpdateException extends RuntimeException {
    public OrderStatusUpdateException(String message) {
        super(message);
    }
}
