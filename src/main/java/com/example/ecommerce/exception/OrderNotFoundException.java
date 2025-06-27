package com.example.ecommerce.exception;

public class OrderNotFoundException extends RuntimeException {
    
    public OrderNotFoundException(Long orderId) {
        super("Commande non trouvée avec l'ID: " + orderId);
    }
    
    public OrderNotFoundException(String orderNumber) {
        super("Commande non trouvée avec le numéro: " + orderNumber);
    }
} 