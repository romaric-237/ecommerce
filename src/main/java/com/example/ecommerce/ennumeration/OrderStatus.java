package com.example.ecommerce.ennumeration;

public enum OrderStatus {
    PENDING("En attente de paiement"),
    PAID("Payée"),
    SHIPPED("Expédiée"),
    DELIVERED("Livrée"),
    CANCELLED("Annulée"),
    FAILED("Échec de paiement");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 