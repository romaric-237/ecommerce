package com.example.ecommerce.controller;

import com.example.ecommerce.controller.dto.ProductUpdateDTO;
import com.example.ecommerce.controller.dto.ProductUpdateResponseDTO;
import com.example.ecommerce.entities.ProductEntity;
import com.example.ecommerce.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/admin/products")
@CrossOrigin(origins = "*")
@PreAuthorize("hasRole('GESTIONNAIRE')")
@Validated
public class AdminProductController {

    private static final Logger logger = LoggerFactory.getLogger(AdminProductController.class);

    @Autowired
    private ProductService productService;

    /**
     * Met à jour un produit (Version admin avec endpoint dédié)
     * @param id ID du produit à mettre à jour
     * @param updateDTO DTO contenant les nouvelles données du produit
     * @return ResponseEntity avec le produit mis à jour
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProductUpdateResponseDTO> updateProduct(
            @PathVariable int id,
            @Valid @RequestBody ProductUpdateDTO updateDTO) {
        
        try {
            logger.info("Tentative de mise à jour du produit avec l'ID: {} par l'utilisateur: {}", 
                       id, getCurrentUsername());
            
            if (id <= 0) {
                logger.warn("ID de produit invalide: {}", id);
                return ResponseEntity.badRequest().build();
            }
            
            // Récupérer le nom d'utilisateur actuel pour l'audit
            String currentUser = getCurrentUsername();
            
            // Appeler le service pour la mise à jour
            ProductUpdateResponseDTO updatedProduct = productService.updateProduct(id, updateDTO, currentUser);
            
            logger.info("Produit mis à jour avec succès: {} par l'utilisateur: {}", 
                       updatedProduct.getNom(), currentUser);
            
            return ResponseEntity.ok(updatedProduct);
            
        } catch (Exception e) {
            logger.error("Erreur lors de la mise à jour du produit avec l'ID {}: {}", 
                        id, e.getMessage(), e);
            throw e; // Laisser le GlobalExceptionHandler gérer l'erreur
        }
    }

    /**
     * Récupère tous les produits (version admin avec plus de détails)
     * @return ResponseEntity avec la liste des produits
     */
    @GetMapping
    public ResponseEntity<List<ProductEntity>> getAllProductsAdmin() {
        try {
            logger.info("Récupération de tous les produits par l'administrateur: {}", getCurrentUsername());
            
            List<ProductEntity> products = productService.getAllProducts();
            
            logger.info("Retour de {} produits pour l'administrateur", products.size());
            return ResponseEntity.ok(products);
            
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des produits: {}", e.getMessage(), e);
            throw e;
        }
    }

    /**
     * Récupère un produit par son ID (version admin)
     * @param id ID du produit
     * @return ResponseEntity avec le produit
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductEntity> getProductByIdAdmin(@PathVariable int id) {
        try {
            logger.info("Récupération du produit avec l'ID: {} par l'administrateur: {}", 
                       id, getCurrentUsername());
            
            if (id <= 0) {
                logger.warn("ID de produit invalide: {}", id);
                return ResponseEntity.badRequest().build();
            }
            
            ProductEntity product = productService.getProductById(id);
            
            logger.info("Produit trouvé par l'administrateur: {}", product.getNom());
            return ResponseEntity.ok(product);
            
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du produit avec l'ID {}: {}", 
                        id, e.getMessage(), e);
            throw e;
        }
    }
    
    /**
     * Récupère le nom d'utilisateur actuel depuis le contexte de sécurité
     * @return Le nom d'utilisateur ou "unknown" si non disponible
     */
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "unknown";
    }
} 