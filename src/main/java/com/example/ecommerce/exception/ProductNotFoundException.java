package com.example.ecommerce.exception;

public class ProductNotFoundException extends RuntimeException {
    
    public ProductNotFoundException(String message) {
        super(message);
    }
    
    public ProductNotFoundException(int productId) {
        super("Produit non trouvé avec l'ID: " + productId);
    }
    
    public ProductNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
} 