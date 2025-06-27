package com.example.ecommerce.exception;

public class InsufficientStockException extends RuntimeException {
    
    public InsufficientStockException(String productName, int requested, int available) {
        super(String.format("Stock insuffisant pour le produit '%s': demandé %d, disponible %d", 
                           productName, requested, available));
    }
    
    public InsufficientStockException(Integer productId, int requested, int available) {
        super(String.format("Stock insuffisant pour le produit ID %d: demandé %d, disponible %d", 
                           productId, requested, available));
    }
} 