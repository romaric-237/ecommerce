package com.example.ecommerce.controller;

import com.example.ecommerce.controller.dto.ProductDTO;
import com.example.ecommerce.controller.dto.ProductUpdateDTO;
import com.example.ecommerce.controller.dto.ProductUpdateResponseDTO;
import com.example.ecommerce.entities.ProductEntity;
import com.example.ecommerce.repositories.ProductRepository;
import com.example.ecommerce.services.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
@Validated
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductRepository productRepository;
    
    @Autowired
    private ProductService productService;

    /**
     * Récupère tous les produits
     * @return ResponseEntity avec la liste des produits
     */
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        try {
            logger.info("Récupération de tous les produits");
            List<ProductEntity> products = productRepository.findAll();
            
            if (products.isEmpty()) {
                logger.info("Aucun produit trouvé");
                return ResponseEntity.noContent().build();
            }
            
            List<ProductDTO> productDTOs = products.stream()
                    .map(ProductDTO::new)
                    .collect(Collectors.toList());
            
            logger.info("Retour de {} produits", productDTOs.size());
            return ResponseEntity.ok(productDTOs);
            
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des produits: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupère un produit par son ID
     * @param id ID du produit
     * @return ResponseEntity avec le produit ou erreur 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable int id) {
        try {
            logger.info("Récupération du produit avec l'ID: {}", id);
            
            if (id <= 0) {
                logger.warn("ID de produit invalide: {}", id);
                return ResponseEntity.badRequest().build();
            }
            
            Optional<ProductEntity> productEntity = productRepository.findById(id);
            
            if (productEntity.isEmpty()) {
                logger.warn("Produit non trouvé avec l'ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            
            ProductDTO productDTO = new ProductDTO(productEntity.get());
            logger.info("Produit trouvé: {}", productDTO.getNom());
            return ResponseEntity.ok(productDTO);
            
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération du produit avec l'ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupère tous les produits d'une catégorie
     * @param id ID de la catégorie
     * @return ResponseEntity avec la liste des produits de la catégorie
     */
    @GetMapping("/category/{id}")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable int id) {
        try {
            logger.info("Récupération des produits pour la catégorie ID: {}", id);
            
            if (id <= 0) {
                logger.warn("ID de catégorie invalide: {}", id);
                return ResponseEntity.badRequest().build();
            }
            
            List<ProductEntity> products = productRepository.findByCategoryId(id);
            
            if (products.isEmpty()) {
                logger.info("Aucun produit trouvé pour la catégorie ID: {}", id);
                return ResponseEntity.noContent().build();
            }
            
            List<ProductDTO> productDTOs = products.stream()
                    .map(ProductDTO::new)
                    .collect(Collectors.toList());
            
            logger.info("Retour de {} produits pour la catégorie ID: {}", productDTOs.size(), id);
            return ResponseEntity.ok(productDTOs);
            
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des produits pour la catégorie ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Recherche des produits par nom (nouveau endpoint utile)
     * @param nom Nom ou partie du nom à rechercher
     * @return ResponseEntity avec la liste des produits correspondants
     */
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProductsByName(@RequestParam String nom) {
        try {
            logger.info("Recherche de produits avec le nom: {}", nom);
            
            if (nom == null || nom.trim().isEmpty()) {
                logger.warn("Paramètre de recherche vide");
                return ResponseEntity.badRequest().build();
            }
            
            List<ProductEntity> products = productRepository.findByNomContainingIgnoreCase(nom.trim());
            
            if (products.isEmpty()) {
                logger.info("Aucun produit trouvé pour la recherche: {}", nom);
                return ResponseEntity.noContent().build();
            }
            
            List<ProductDTO> productDTOs = products.stream()
                    .map(ProductDTO::new)
                    .collect(Collectors.toList());
            
            logger.info("Retour de {} produits pour la recherche: {}", productDTOs.size(), nom);
            return ResponseEntity.ok(productDTOs);
            
        } catch (Exception e) {
            logger.error("Erreur lors de la recherche de produits avec le nom {}: {}", nom, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Met à jour un produit (Réservé aux gestionnaires)
     * @param id ID du produit à mettre à jour
     * @param updateDTO DTO contenant les nouvelles données du produit
     * @return ResponseEntity avec le produit mis à jour ou erreur
     */
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('GESTIONNAIRE')")
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
