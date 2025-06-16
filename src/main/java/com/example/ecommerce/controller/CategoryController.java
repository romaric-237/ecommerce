package com.example.ecommerce.controller;

import com.example.ecommerce.controller.dto.CategoryDTO;
import com.example.ecommerce.controller.dto.ProductDTO;
import com.example.ecommerce.entities.CategoryEntity;
import com.example.ecommerce.entities.ProductEntity;
import com.example.ecommerce.repositories.CategoryRepository;
import com.example.ecommerce.repositories.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class CategoryController {

    private static final Logger logger = LoggerFactory.getLogger(CategoryController.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    /**
     * Récupère toutes les catégories
     * @return ResponseEntity avec la liste des catégories
     */
    @GetMapping("/categories")
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        try {
            logger.info("Récupération de toutes les catégories");
            List<CategoryEntity> categories = categoryRepository.findAll();
            
            if (categories.isEmpty()) {
                logger.info("Aucune catégorie trouvée");
                return ResponseEntity.noContent().build();
            }
            
            List<CategoryDTO> categoryDTOs = categories.stream()
                    .map(CategoryDTO::new)
                    .collect(Collectors.toList());
            
            logger.info("Retour de {} catégories", categoryDTOs.size());
            return ResponseEntity.ok(categoryDTOs);
            
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération des catégories: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupère une catégorie par son ID
     * @param id ID de la catégorie
     * @return ResponseEntity avec la catégorie ou erreur 404
     */
    @GetMapping("/category/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable int id) {
        try {
            logger.info("Récupération de la catégorie avec l'ID: {}", id);
            
            if (id <= 0) {
                logger.warn("ID de catégorie invalide: {}", id);
                return ResponseEntity.badRequest().build();
            }
            
            Optional<CategoryEntity> categoryEntity = categoryRepository.findById(id);
            
            if (categoryEntity.isEmpty()) {
                logger.warn("Catégorie non trouvée avec l'ID: {}", id);
                return ResponseEntity.notFound().build();
            }
            
            CategoryDTO categoryDTO = new CategoryDTO(categoryEntity.get());
            logger.info("Catégorie trouvée: {}", categoryDTO.getNom());
            return ResponseEntity.ok(categoryDTO);
            
        } catch (Exception e) {
            logger.error("Erreur lors de la récupération de la catégorie avec l'ID {}: {}", id, e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Récupère tous les produits d'une catégorie
     * @param id ID de la catégorie
     * @return ResponseEntity avec la liste des produits de la catégorie
     */
    @GetMapping("/category/{id}/products")
    public ResponseEntity<List<ProductDTO>> getProductsByCategory(@PathVariable int id) {
        try {
            logger.info("Récupération des produits pour la catégorie ID: {}", id);
            
            if (id <= 0) {
                logger.warn("ID de catégorie invalide: {}", id);
                return ResponseEntity.badRequest().build();
            }
            
            // Vérifier que la catégorie existe
            if (!categoryRepository.existsById(id)) {
                logger.warn("Catégorie non trouvée avec l'ID: {}", id);
                return ResponseEntity.notFound().build();
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
}
