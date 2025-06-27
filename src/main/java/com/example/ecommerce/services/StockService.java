package com.example.ecommerce.services;

import com.example.ecommerce.entities.ProductEntity;
import com.example.ecommerce.exception.InsufficientStockException;
import com.example.ecommerce.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StockService {
    private final ProductRepository productRepository;

    /**
     * Vérifie que le stock est suffisant pour un produit
     */
    public void checkStock(ProductEntity product, int requestedQuantity) {
        // Supposons qu'il y a un champ stock dans ProductEntity (à ajouter si besoin)
        Integer stock = product.getStock();
        if (stock == null) stock = 0;
        if (requestedQuantity > stock) {
            throw new InsufficientStockException(product.getNom(), requestedQuantity, stock);
        }
    }

    /**
     * Décrémente le stock d'un produit après commande
     */
    @Transactional
    public void decrementStock(ProductEntity product, int quantity) {
        Integer stock = product.getStock();
        if (stock == null) stock = 0;
        if (quantity > stock) {
            throw new InsufficientStockException(product.getNom(), quantity, stock);
        }
        product.setStock(stock - quantity);
        productRepository.save(product);
    }
} 