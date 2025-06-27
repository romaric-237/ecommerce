package com.example.ecommerce.exception;

public class PaymentFailedException extends RuntimeException {
    
    public PaymentFailedException(String message) {
        super("Échec du paiement: " + message);
    }
    
    public PaymentFailedException(String message, Throwable cause) {
        super("Échec du paiement: " + message, cause);
    }
} 