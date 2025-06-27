package com.example.ecommerce.ennumeration;

public enum PaymentMethod {
    CREDIT_CARD("Carte de crédit"),
    DEBIT_CARD("Carte de débit"),
    BANK_TRANSFER("Virement bancaire"),
    PAYPAL("PayPal"),
    PAYSAFE_CARD("Paysafe card");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
} 