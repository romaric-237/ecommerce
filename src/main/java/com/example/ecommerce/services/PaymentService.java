package com.example.ecommerce.services;

import com.example.ecommerce.ennumeration.PaymentMethod;
import com.example.ecommerce.exception.PaymentFailedException;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {
    /**
     * Simule le paiement selon la méthode choisie
     * Retourne true si le paiement est accepté, sinon lance une exception
     */
    public boolean processPayment(PaymentMethod method, Double amount) {
        // Ici, on simule toujours le succès sauf pour un montant de 0 ou négatif
        if (amount == null || amount <= 0) {
            throw new PaymentFailedException("Montant de paiement invalide");
        }
        // On pourrait simuler un échec aléatoire ou selon la méthode
        // Pour l'instant, on accepte tout
        return true;
    }
} 