package com.example.ecommerce.services;

import com.example.ecommerce.controller.dto.CategoryDTO;
import com.example.ecommerce.controller.dto.ProductUpdateDTO;
import com.example.ecommerce.controller.dto.ProductUpdateResponseDTO;
import com.example.ecommerce.entities.CategoryEntity;
import com.example.ecommerce.entities.ProductEntity;
import com.example.ecommerce.exception.CategoryNotFoundException;
import com.example.ecommerce.exception.ProductNotFoundException;
import com.example.ecommerce.repositories.CategoryRepository;
import com.example.ecommerce.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {
    
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    
    /**
     * Récupère tous les produits
     */
    public List<ProductEntity> getAllProducts() {
        return productRepository.findAll();
    }
    
    /**
     * Récupère un produit par son ID
     */
    public ProductEntity getProductById(int id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
    
    /**
     * Met à jour un produit
     */
    @Transactional
    public ProductUpdateResponseDTO updateProduct(int productId, ProductUpdateDTO updateDTO, String updatedBy) {
        // 1. Vérifier que le produit existe
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        
        // 2. Vérifier que la catégorie existe
        CategoryEntity category = categoryRepository.findById(updateDTO.getCategoryId())
                .orElseThrow(() -> new CategoryNotFoundException(updateDTO.getCategoryId()));
        
        // 3. Mettre à jour les champs du produit
        product.setNom(updateDTO.getNom());
        product.setDescription(updateDTO.getDescription());
        product.setMarque(updateDTO.getMarque());
        product.setPrix(updateDTO.getPrix());
        product.setThumbnailUrl(updateDTO.getThumbnailUrl());
        product.setGenre(updateDTO.getGenre());
        product.setTaille(updateDTO.getTaille());
        product.setCategory(category);
        
        // 4. Mettre à jour les champs d'audit
        product.setUpdatedAt(LocalDateTime.now());
        product.setUpdatedBy(updatedBy);
        
        // 5. Sauvegarder les modifications
        ProductEntity savedProduct = productRepository.save(product);
        
        // 6. Créer la réponse DTO
        CategoryDTO categoryDTO = new CategoryDTO(
            category.getId(),
            category.getNom(),
            category.getDescription()
        );
        
        return new ProductUpdateResponseDTO(
            savedProduct.getId(),
            savedProduct.getNom(),
            savedProduct.getDescription(),
            savedProduct.getMarque(),
            savedProduct.getPrix(),
            savedProduct.getThumbnailUrl(),
            savedProduct.getGenre(),
            savedProduct.getTaille(),
            categoryDTO,
            savedProduct.getUpdatedAt()
        );
    }
    
    /**
     * Récupère les produits par catégorie
     */
    public List<ProductEntity> getProductsByCategory(int categoryId) {
        return productRepository.findByCategoryId(categoryId);
    }
    
    /**
     * Recherche des produits par nom
     */
    public List<ProductEntity> searchProductsByName(String name) {
        return productRepository.findByNomContainingIgnoreCase(name);
    }
    
    /**
     * Vérifie si un produit existe
     */
    public boolean productExists(int productId) {
        return productRepository.existsById(productId);
    }
} 